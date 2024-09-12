package com.lazydeveloper.trelloplex.presentation.screen.details

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.lazydeveloper.data.Resource
import com.lazydeveloper.database.entities.HistoryEntity
import com.lazydeveloper.network.model.DetailsResponse
import com.lazydeveloper.network.model.FireStoreServer
import com.lazydeveloper.network.model.FttpMoviePlalyerResponse
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.trelloplex.navigation.Screen
import com.lazydeveloper.trelloplex.presentation.composables.AnimatedPreloader
import com.lazydeveloper.trelloplex.presentation.composables.CustomImage
import com.lazydeveloper.trelloplex.presentation.composables.CustomImageAsync
import com.lazydeveloper.trelloplex.presentation.composables.CustomText
import com.lazydeveloper.trelloplex.presentation.composables.showInterstitial
import com.lazydeveloper.trelloplex.presentation.composables.showInterstitial2
import com.lazydeveloper.trelloplex.ui.theme.Background_Black
import com.lazydeveloper.trelloplex.ui.theme.Loading_Orange
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: MovieDetailsScreenViewModel = hiltViewModel(),
    id: Int,
) {
    val context = LocalContext.current

    val movieDetailsState by viewModel.movieDetailsFlow.collectAsState()

    var isServerAvailable by remember { mutableStateOf(true) }
    val db = FirebaseFirestore.getInstance()
    var response: FireStoreServer

    val movieModel by remember { mutableStateOf<FttpMoviePlalyerResponse?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchMovieDetails(id)
        viewModel.fetchExternalMovieIds(id)
        try {
            val data = db.collection("data").get().await()
            data.map {
                response = it.toObject(FireStoreServer::class.java)
                isServerAvailable = response.player
            }
        } catch (e: Exception) {
            Log.e("FirebaseInit", "onCreate: ${e.message}")
        }
    }

    movieDetailsState.let {
        when (it) {
            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    LinearProgressIndicator(
                        color = Loading_Orange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    )
                }
            }

            is Resource.Success -> {
                val movie = it.data

                DetailsScreenContent(
                    movie = movie,
                    movieWatchFileDetails = movieModel ?: FttpMoviePlalyerResponse(),
                    navController = navController,
                    viewModel = viewModel,
                    isServerAvailable = mutableStateOf(isServerAvailable),
                    onClick = {
                        val randomInt = Random.nextInt(1, 6)
                        if (randomInt == 3) {
                            showInterstitial(context,
                                onAdDismissed = { navController.popBackStack() },
                                onError = {
                                    showInterstitial2(
                                        context,
                                        onAdDismissed = { navController.popBackStack() },
                                        onError = { navController.popBackStack() }
                                    )
                                }
                            )
                        } else {
                            navController.popBackStack()
                        }
                    },
                    onClick2 = {

                    }
                )
            }

            is Resource.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AnimatedPreloader()
                }
            }
        }
    }
}
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewDialog(
    showDialog: MutableState<Boolean>,
    url: String,
    onDismiss: () -> Unit = {}
) {
    var showDismissButton by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(15) } // countdown starts from 5

    LaunchedEffect(key1 = showDialog.value) {
        if (showDialog.value) {
            while (countdown > 0) {
                delay(1000L) // delay for 1 second
                countdown--
            }
            showDismissButton = true
        }
    }

    if (showDialog.value) {
        Dialog(onDismissRequest = {}) { // Removed the onDismissRequest parameter
            // Use `Box` for the Dialog's container
            Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            this.settings.javaScriptEnabled = true
                            this.webViewClient = WebViewClient() // Set WebViewClient
                            this.loadUrl(url)
                        }
                    },
                    update = { webView -> webView.loadUrl(url) }
                )
                if (showDismissButton) {
                    Button(onClick = {
                        showDialog.value = false
                        onDismiss.invoke()
                    }) {
                        Text("Dismiss")
                    }
                } else {
                    Box(modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .background(Color.White, RoundedCornerShape(15.dp))
                        .clip(RoundedCornerShape(15.dp))
                    ){
                        Text(
                            "$countdown",
                            Modifier.align(Alignment.Center),
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreenContent(
    movie: DetailsResponse,
    movieWatchFileDetails: FttpMoviePlalyerResponse = FttpMoviePlalyerResponse(),
    navController: NavController,
    viewModel: MovieDetailsScreenViewModel? = null,
    isServerAvailable: MutableState<Boolean>,
    onClick: () -> Unit = {},
    onClick2: () -> Unit = {},
) {
    val context: Context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()

    val isEmpty = viewModel?.mPref?.appVersion
    val bolgSite = viewModel?.mPref?.blogSite

    var showBottomSheet by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }

    var isFavorite by remember { mutableStateOf(false) }

    var showDialogSite by remember { mutableStateOf(false) }

    // This is where you call the WebViewDialog
    if(showDialogSite) {
        WebViewDialog(
            showDialog = mutableStateOf(true),
            url = bolgSite.toString(),
            onDismiss = {
                showDialogSite = false
                showBottomSheet = false
                showDialog = true
            }
        )
    }


    if (showDialog) {
        if (isServerAvailable.value) {
            AlertDialog(
                backgroundColor = Color(0xFF444444),
                onDismissRequest = { showDialog = false },
                text = {
                    CustomText(
                        text = "Select one of the servers below",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                    )
                },
                buttons = {
                    val genresList: List<String> =
                        movie.genres?.map { it?.name.toString() } ?: emptyList()
                    val gson = Gson()
                    val json = gson.toJson(genresList)
                    val sdf = SimpleDateFormat("h:mm a / d MMM yyyy", Locale.getDefault())
                    val currentDate = sdf.format(Date())
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                            .fillMaxWidth(),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .padding(horizontal = 6.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .clickable {
                                    viewModel?.addHistory(
                                        HistoryEntity(
                                            movieId = movie.id.toString(),
                                            movieName = movie.title.toString(),
                                            type = "movie",
                                            watchDate = currentDate.toString(),
                                            poster = movie.posterPath.toString(),
                                            year = if (movie.releaseDate?.length!! >= 4) movie.releaseDate
                                                .toString()
                                                .substring(
                                                    0,
                                                    4
                                                ) else movie.releaseDate.toString(),
                                            region = movie.productionCountries?.getOrNull(0)?.name
                                                ?: "",
                                            genre = json,
                                        )
                                    )
                                    showBottomSheet = false
                                    showDialog = false
                                    navController.navigate(
                                        Screen.PlayerScreen.passArguments(
                                            movie.id ?: 0,
                                            2
                                        )
                                    )
                                }
                                .border(1.dp, Loading_Orange, shape = RoundedCornerShape(5.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(start = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CustomImage(
                                    imageId = R.drawable.ic_server,
                                    contentDescription = "server",
                                    modifier = Modifier
                                        .size(28.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(start = 16.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CustomText(
                                        text = "Server 1",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.W500,
                                        modifier = Modifier.padding(top = 1.dp, end = 10.dp)
                                    )
                                    CustomText(
                                        text = "Dubbed in English",
                                        color = Color(0xFF919191),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.W400,
                                        modifier = Modifier.padding(top = 0.dp, end = 2.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .padding(horizontal = 6.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .clickable {
                                    showBottomSheet = false
                                    showDialog = false
                                    viewModel?.addHistory(
                                        HistoryEntity(
                                            movieId = movie.id.toString(),
                                            movieName = movie.title.toString(),
                                            type = "movie",
                                            watchDate = currentDate.toString(),
                                            poster = movie.posterPath.toString(),
                                            year = if (movie.releaseDate?.length!! >= 4) movie.releaseDate
                                                .toString()
                                                .substring(
                                                    0,
                                                    4
                                                ) else movie.releaseDate.toString(),
                                            region = movie.productionCountries?.getOrNull(0)?.name
                                                ?: "",
                                            genre = json,
                                        )
                                    )
                                    navController.navigate(
                                        Screen.PlayerScreen.passArguments(
                                            movie.id ?: 0,
                                            serverId = 1
                                        )
                                    )
                                }
                                .border(1.dp, Loading_Orange, shape = RoundedCornerShape(5.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(start = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CustomImage(
                                    imageId = R.drawable.ic_server,
                                    contentDescription = "server",
                                    modifier = Modifier
                                        .size(28.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(start = 16.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CustomText(
                                        text = "Server 2",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.W500,
                                        modifier = Modifier.padding(top = 1.dp, end = 10.dp)
                                    )
                                    CustomText(
                                        text = "Dubbed in English",
                                        color = Color(0xFF919191),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.W400,
                                        modifier = Modifier.padding(top = 0.dp, end = 2.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            )
        }
    }

    if (showDownloadDialog) {
        if (isServerAvailable.value) {
            AlertDialog(
                backgroundColor = Color(0xFF444444),
                onDismissRequest = { showDownloadDialog = false },
                text = {
                    Column {
                        CustomText(
                            text = movieWatchFileDetails[0].movieTitle.toString(),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                        )
                        CustomText(
                            text = "${movieWatchFileDetails[0].movieQuality.toString()} - ${movieWatchFileDetails[0].movieSize.toString()}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400,
                        )
                    }
                },
                buttons = {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .width(135.dp)
                                .height(46.dp)
                                .padding(horizontal = 6.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .clickable {
                                    showDownloadDialog = false
                                }
                                .border(1.dp, Loading_Orange, shape = RoundedCornerShape(5.dp))
                        ) {
                            CustomText(
                                text = "Cancel",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W500,
                                modifier = Modifier
                                    .padding(top = 1.dp, end = 10.dp)
                                    .align(Alignment.Center)
                            )

                        }

                        Box(
                            modifier = Modifier
                                .width(135.dp)
                                .height(46.dp)
                                .padding(horizontal = 6.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .clickable {
/*                                    if (mPref.getString(
                                            CommonEnum.VIDEO_SOURCE.toString(),
                                            "error"
                                        ) == "error"
                                    ) {
                                        showDownloadDialog = false
                                        Toast
                                            .makeText(
                                                context,
                                                "Source not found. Please try again later.",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()

                                    } else {
                                        Toast
                                            .makeText(
                                                context,
                                                "Downloading ${movie.title}.mp4",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                        context.downloadFile(
                                            mPref.getString(
                                                CommonEnum.VIDEO_SOURCE.toString(),
                                                ""
                                            )!!,
                                            "${movie.title}.mp4"
                                        )
                                        showDownloadDialog = false
                                    }*/
                                }
                                .border(1.dp, Loading_Orange, shape = RoundedCornerShape(5.dp))
                        ) {
                            CustomText(
                                text = "Download",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W500,
                                modifier = Modifier
                                    .padding(top = 1.dp, end = 10.dp)
                                    .align(Alignment.Center)
                            )

                        }
                    }
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            if (isEmpty != "") {
                CustomImage(
                    imageId = R.drawable.ic_logo,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxSize(),
                    contentDescription = "ImageRequest example",
                )
            } else {
                CustomImageAsync(
                    imageUrl = "${viewModel.mPref.tmdbImagePathBackCover}${movie.backdropPath}",
                    size = 512,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentDescription = "ImageRequest example",
                )
            }

            CustomImage(
                imageId = R.drawable.ic_back,
                modifier = Modifier
                    .size(60.dp)
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .clickable { onClick.invoke() },
                contentDescription = ""
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFF1A1A1A),
                            ),
                            startY = 20f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .clip(RoundedCornerShape(8.dp)),
                verticalAlignment = Alignment.CenterVertically

            ) {
                if (isEmpty != "") {
                    CustomImage(
                        imageId = R.drawable.ic_logo,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .fillMaxSize(),
                        contentDescription = "ImageRequest example",
                    )
                } else {
                    CustomImageAsync(
                        imageUrl = "${viewModel.mPref.tmdbImagePath}${movie.posterPath}",
                        size = 512,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(130.dp)
                            .padding(start = 16.dp, end = 8.dp),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = "ImageRequest example",
                    )
                }

                Column {
                    CustomText(
                        text = movie.title!!,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W400,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomImage(
                            imageId = R.drawable.ic_star,
                            contentDescription = "",
                            modifier = Modifier
                                .size(22.dp)
                                .padding(start = 8.dp)
                        )
                        CustomText(
                            text = " ${
                                if (movie.voteAverage!! >= 3) movie.voteAverage.toString()
                                    .substring(0, 3) else movie.voteAverage.toString()
                            }",
                            color = Color(0xFFB1B0B0),
                            fontSize = 12.sp
                        )
                        CustomText(
                            text = " (${movie.voteCount} votes) • ",
                            color = Color(0xFFB1B0B0),
                            fontSize = 12.sp
                        )
                        CustomText(
                            text = if (movie.releaseDate?.length!! >= 4) movie.releaseDate.toString()
                                .substring(0, 4) else movie.releaseDate.toString(),
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(145.dp)
                    .height(43.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Loading_Orange)
                    .clickable {
                        showBottomSheet = true
                    }
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    CustomText(
                        text = "Watch",
                        color = Background_Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier
                            .padding(top = 1.dp)
                    )
                    Image(
                        painterResource(id = R.drawable.ic_play), contentDescription = "Back",
                        modifier = Modifier
                            .size(20.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(145.dp)
                    .height(43.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable {
                        Toast
                            .makeText(
                                context,
                                "Coming soon! Please try again later.",
                                Toast.LENGTH_SHORT
                            )
                            .show()
//                        if (isEmpty != "") {
//                            Toast
//                                .makeText(
//                                    context,
//                                    "Coming soon! Please try again later.",
//                                    Toast.LENGTH_SHORT
//                                )
//                                .show()
//                        } else {
//                            showInterstitial(
//                                context,
//                                onAdDismissed = {
//                                    if (movieWatchFileDetails?.isNotEmpty() == true) {
//                                        showDownloadDialog = true
//                                    } else {
//                                        Toast
//                                            .makeText(
//                                                context,
//                                                "Source not found. Please try again later.",
//                                                Toast.LENGTH_SHORT
//                                            )
//                                            .show()
//                                    }
//                                },
//                                onError = {
//                                    showInterstitial2(
//                                        context,
//                                        onAdDismissed = {
//                                            if (movieWatchFileDetails?.isNotEmpty() == true) {
//                                                showDownloadDialog = true
//                                            } else {
//                                                Toast
//                                                    .makeText(
//                                                        context,
//                                                        "Source not found. Please try again later.",
//                                                        Toast.LENGTH_SHORT
//                                                    )
//                                                    .show()
//                                            }
//                                        },
//                                        onError = {
//                                            if (movieWatchFileDetails?.isNotEmpty() == true) {
//                                                showDownloadDialog = true
//                                            } else {
//                                                Toast
//                                                    .makeText(
//                                                        context,
//                                                        "Source not found. Please try again later.",
//                                                        Toast.LENGTH_SHORT
//                                                    )
//                                                    .show()
//                                            }
//                                        }
//                                    )
//                                }
//                            )
//                        }
                    }
                    .border(1.dp, Loading_Orange, shape = RoundedCornerShape(5.dp))
            ) {
                Row(modifier = Modifier.align(Alignment.Center)) {
                    CustomText(
                        text = "Download",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.padding(top = 1.dp, end = 2.dp)
                    )
                    CustomImage(
                        imageId = R.drawable.ic_download,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(18.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        CustomText(
            text = "Overview",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(16.dp)
        )
        CustomText(
            text = movie.overview!!,
            color = Color(0xFFBDBBBB),
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        CustomText(
            text = "Genre",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(16.dp)
        )
        CustomText(
            text = movie.genres?.joinToString(separator = ", ") { it?.name.toString() } ?: "",
            color = Color(0xFFBDBBBB),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        CustomText(
            text = "Production Companies",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(16.dp)
        )
        CustomText(
            text = movie.productionCompanies?.joinToString(separator = ", ") { it?.name.toString() }
                ?: "",
            color = Color(0xFFBDBBBB),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        CustomText(
            text = "Production Countries",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(16.dp)
        )
        CustomText(
            text = movie.productionCountries?.joinToString(separator = ", ") { it?.name.toString() }
                ?: "",
            color = Color(0xFFBDBBBB),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )


    }
    if (showBottomSheet)
    {
        ModalBottomSheet(
            containerColor = Color(0xFF1D2125),
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .height(350.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomText(
                    text = "Play '${movie.title}'",
                    color = Color(0xFFBDBBBB),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W300,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )
                CustomText(
                    text = "Select Player",
                    color = Color(0xFFBDBBBB),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .clickable {
                            if(viewModel?.mPref?.blogSite != ""){
                                showDialogSite = true
                            }else{
                                showInterstitial(
                                    context,
                                    onAdDismissed = {
                                        showBottomSheet = false
                                        showDialog = true
                                    },
                                    onError = {
                                        showInterstitial2(
                                            context,
                                            onAdDismissed = {
                                                showBottomSheet = false
                                                showDialog = true
                                            },
                                            onError = {
                                                showBottomSheet = false
                                                showDialog = true
                                            }
                                        )
                                    }
                                )
                            }
                        }
                        .border(1.dp, Loading_Orange, shape = RoundedCornerShape(5.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomImage(
                            imageId = R.drawable.ic_logo,
                            contentDescription = "logo",
                            modifier = Modifier
                                .size(35.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row {
                                CustomText(
                                    text = "CLVplex",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.W500,
                                    modifier = Modifier.padding(top = 1.dp, end = 10.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .width(75.dp)
                                        .height(15.dp)
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(Loading_Orange)
                                ) {
                                    CustomText(
                                        text = "RECOMMENDED",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .padding(2.dp)
                                    )
                                }
                            }

                            Row {
                                CustomText(
                                    text = "FREE",
                                    color = Color(0xFF919191),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.W400,
                                    modifier = Modifier.padding(top = 2.dp, end = 2.dp)
                                )
                                LazyRow(
                                    content = {
                                        items(5) {
                                            CustomImage(
                                                imageId = R.drawable.ic_star,
                                                contentDescription = "star",
                                                modifier = Modifier
                                                    .size(15.dp)
                                                    .padding(top = 2.dp),
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .padding(horizontal = 20.dp)
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .clickable {
                            Toast
                                .makeText(
                                    context,
                                    "Not available at the moment",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                        .border(1.dp, Loading_Orange, shape = RoundedCornerShape(5.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomImage(
                            imageId = R.drawable.ic_external,
                            contentDescription = "pokemon",
                            modifier = Modifier
                                .size(35.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            CustomText(
                                text = "External Player",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W500,
                                modifier = Modifier.padding(top = 1.dp, end = 2.dp)
                            )
                            CustomText(
                                text = "Coming soon",
                                color = Color(0xFF919191),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400,
                                modifier = Modifier.padding(top = 1.dp, end = 2.dp)
                            )
                        }
                    }
                }
                CustomText(
                    text = "Select and play the movie",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier
                        .padding(top = 1.dp, end = 2.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 50.dp)
                )
            }
        }
    }
}

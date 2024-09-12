package com.lazydeveloper.trelloplex.presentation.screen.details

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.lazydeveloper.data.Resource
import com.lazydeveloper.database.entities.HistoryEntity
import com.lazydeveloper.network.model.FireStoreServer
import com.lazydeveloper.network.model.SeriesDetailsResponse
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.trelloplex.navigation.Screen
import com.lazydeveloper.trelloplex.presentation.composables.AnimatedPreloader
import com.lazydeveloper.trelloplex.presentation.composables.CustomEpisodeItem
import com.lazydeveloper.trelloplex.presentation.composables.CustomImage
import com.lazydeveloper.trelloplex.presentation.composables.CustomImageAsync
import com.lazydeveloper.trelloplex.presentation.composables.CustomText
import com.lazydeveloper.trelloplex.presentation.composables.showInterstitial
import com.lazydeveloper.trelloplex.presentation.composables.showInterstitial2
import com.lazydeveloper.trelloplex.ui.theme.Background_Black
import com.lazydeveloper.trelloplex.ui.theme.Loading_Orange
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Composable
fun SeriesDetailsScreen(
    navController: NavController,
    viewModel: SeriesDetailsScreenViewModel = hiltViewModel(),
    id: Int,
) {
    val context = LocalContext.current
    viewModel.fetchPosts(id)
    val networkState by viewModel.seriesDetailsFlow.collectAsState()

    networkState.let {
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
                SeriesDetailsScreenContent(
                    movie = movie,
                    navController = navController,
                    viewModel = viewModel
                ) {
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
                }
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SeriesDetailsScreenContent(
    movie: SeriesDetailsResponse,
    navController: NavController,
    viewModel: SeriesDetailsScreenViewModel,
    onClick: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()
    val context: Context = LocalContext.current
//    val mPref = context.getSharedPrefs()
    var showBottomSheet by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    var mutable by remember { mutableIntStateOf(0) }
    var episodeIndex by remember { mutableIntStateOf(0) }

    val selectedSeason = remember { mutableStateOf("1") }
    val languageExpand = remember { mutableStateOf(false) }

    val isEmpty = viewModel.mPref.appVersion
    val bolgSite = viewModel.mPref.blogSite

    val selectedLanguageIndex = remember { mutableIntStateOf(0) }
    val sdf = SimpleDateFormat("h:mm a / d MMM yyyy", Locale.getDefault())
    val currentDate = sdf.format(Date())

    var showDialog by remember { mutableStateOf(false) }

    var isServerAvailable by remember { mutableStateOf(true) }
    val db = FirebaseFirestore.getInstance()
    var response: FireStoreServer

    LaunchedEffect(Unit) {
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
        if (isServerAvailable) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                text = {
                    CustomText(
                        text = "Select one of the servers below",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                    )
                },
                backgroundColor = Color(0xFF444444),
                buttons = {
                    val genresList: List<String> =
                        movie.genres?.map { it?.name.toString() } ?: emptyList()
                    val gson = Gson()
                    val json = gson.toJson(genresList)
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .padding(horizontal = 6.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .clickable {
                                    showBottomSheet = false
                                    showDialog = false
                                    viewModel.addHistory(
                                        HistoryEntity(
                                            movieId = movie.id.toString(),
                                            movieName = movie.name.toString(),
                                            type = "tv",
                                            watchDate = currentDate.toString(),
                                            poster = movie.posterPath.toString(),
                                            year = if (movie.firstAirDate?.length!! >= 4) movie.firstAirDate
                                                .toString()
                                                .substring(
                                                    0,
                                                    4
                                                ) else movie.firstAirDate.toString(),
                                            region = movie.productionCountries?.getOrNull(0)?.name
                                                ?: "",
                                            genre = json,
                                        )
                                    )

                                    navController.navigate(
                                        Screen.SeriesPlayerScreen.passArguments(
                                            comicId = movie.id ?: 0,
                                            seasonId = selectedSeason.value.toInt(),
                                            episodeId = viewModel.mPref.selectedEpisode,
                                            serverId = 2
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
                                    viewModel.addHistory(
                                        HistoryEntity(
                                            movieId = movie.id.toString(),
                                            movieName = movie.name.toString(),
                                            type = "tv",
                                            watchDate = currentDate.toString(),
                                            poster = movie.posterPath.toString(),
                                            year = if (movie.firstAirDate?.length!! >= 4) movie.firstAirDate
                                                .toString()
                                                .substring(
                                                    0,
                                                    4
                                                ) else movie.firstAirDate.toString(),
                                            region = movie.productionCountries?.getOrNull(0)?.name
                                                ?: "",
                                            genre = json,
                                        )
                                    )
                                    navController.navigate(
                                        Screen.SeriesPlayerScreen.passArguments(
                                            comicId = movie.id ?: 0,
                                            seasonId = selectedSeason.value.toInt(),
                                            episodeId = viewModel.mPref.selectedEpisode,
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
                    contentDescription = "logo",
                )
            } else {
                CustomImageAsync(
                    imageUrl = "${viewModel.mPref.tmdbImagePathBackCover}${movie.backdropPath}",
                    size = 512,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = "background cover",
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
                        contentDescription = "logo",
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
                        contentDescription = "poster",
                    )
                }

                Column {
                    CustomText(
                        text = movie.name!!,
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
                                if (movie.voteAverage!! >= 3) movie.firstAirDate.toString()
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
                            text = if (movie.firstAirDate?.length!! >= 4) movie.firstAirDate.toString()
                                .substring(0, 4) else movie.firstAirDate.toString(),
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

            Column {
                FilterOption(
                    title = "Languages",
                    dropdownTitle = "Season ${selectedSeason.value}",
                    expand = languageExpand
                ) {
                    languageExpand.value = !languageExpand.value
                }


                var index = CustomDropDown(
                    dataList = movie.seasons?.map { it?.seasonNumber.toString() }!!,
                    name = movie.seasons!!.map { it?.name.toString() },
                    selectedText = selectedSeason,
                    expanded = languageExpand,
                    selectedIndex = selectedLanguageIndex
                )
                mutable = index
            }

            Box(
                modifier = Modifier
                    .width(155.dp)
                    .height(51.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable {
                        Toast
                            .makeText(
                                context,
                                "Download will be available soon.",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                    .padding()
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
                        contentDescription = "download",
                        modifier = Modifier
                            .size(18.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(pagerState.pageCount) { index ->
                Text(
                    text = getPageTitle(index),
                    color = if (pagerState.currentPage == index) Loading_Orange else Color(
                        0xFF7A7A7A
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                        .padding(16.dp)
                )
            }
        }

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> episodeIndex = ScreenOneContent(episodeCount = mutable, model = movie, viewModel = viewModel) {
//                    showInterstitial(context) {
                    showBottomSheet = true
//                    }
                }

                1 -> ScreenTwoContent(movie)
            }
        }


    }
    if (showBottomSheet)
        ModalBottomSheet(
            containerColor = Color(0xFF1D2125),
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .height(350.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomText(
                    text = "Play '${movie.name}'",
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
//                            isServerSelected.value = true
                            if(viewModel.mPref.blogSite != ""){
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
                    text = "Select and play the episode",
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

@Composable
fun ScreenOneContent(
    episodeCount: Int,
    model: SeriesDetailsResponse,
    viewModel: SeriesDetailsScreenViewModel,
    onClick: () -> Unit = {}
): Int {
    var index = 0
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(model.seasons?.get(episodeCount)?.episodeCount!!) {
            index = CustomEpisodeItem(it, model, viewModel = viewModel) {
                onClick.invoke()
            }
        }
    }
    return index
}

@Composable
fun ScreenTwoContent(movie: SeriesDetailsResponse) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
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
}

@Composable
fun getPageTitle(page: Int): String {
    return when (page) {
        0 -> "Episodes"
        1 -> "Overview"
        else -> "Unknown"
    }
}

@Composable
fun FilterOption(
    title: String,
    dropdownTitle: String,
    topPadding: Dp = 0.dp,
    expand: MutableState<Boolean> = mutableStateOf(false),
    yearFilter: Boolean = false,
    onClick: () -> Unit
) {
    Spacer(modifier = Modifier.padding(top = topPadding))
    Card(
        modifier = Modifier
            .width(155.dp)
            .height(52.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Loading_Orange),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick.invoke() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomText(
                modifier = Modifier.padding(end = 2.dp),
                text = dropdownTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                color = Color.White
            )
            if (yearFilter) {
                Icon(
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp),
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "year_icon"
                )
            } else {
                Icon(
                    modifier = Modifier
                        .rotate(if (expand.value) 90f else 0f)
                        .height(15.dp)
                        .width(15.dp),
                    painter = painterResource(id = R.drawable.ic_play),
                    tint = Color.White,
                    contentDescription = "forward_arrow"
                )
            }
        }
    }
}

@Composable
fun CustomDropDown(
    dataList: List<String>,
    name: List<String>,
    selectedIndex: MutableState<Int>,
    selectedText: MutableState<String>,
    expanded: MutableState<Boolean>
): Int {
    val density = LocalDensity.current.density
    DropdownMenu(
        expanded = expanded.value,
        modifier = Modifier
            .width(180.dp)
            .background(Background_Black)
            .padding(top = 6.dp)
            .border(1.dp, Loading_Orange),
        onDismissRequest = { expanded.value = false })
    {
        dataList.forEachIndexed { index, item ->
            DropdownMenuItem(
                onClick = {
                    selectedText.value = dataList[index]
                    expanded.value = false
                    selectedIndex.value = index
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CustomText(
//                    text = "Season ${index + 1}",
                    text = name[index],
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
    return selectedIndex.value
}
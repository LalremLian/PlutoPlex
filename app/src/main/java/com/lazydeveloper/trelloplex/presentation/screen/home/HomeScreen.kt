package com.lazydeveloper.trelloplex.presentation.screen.home

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.lazydeveloper.data.Resource
import com.lazydeveloper.network.model.FireStoreServer
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.trelloplex.navigation.Screen
import com.lazydeveloper.trelloplex.presentation.composables.AnimatedPreloader
import com.lazydeveloper.trelloplex.presentation.composables.CustomAlertDialog
import com.lazydeveloper.trelloplex.presentation.composables.CustomImage
import com.lazydeveloper.trelloplex.presentation.composables.CustomImageAsync
import com.lazydeveloper.trelloplex.presentation.composables.CustomImageCarousel
import com.lazydeveloper.trelloplex.presentation.composables.CustomText
import com.lazydeveloper.trelloplex.presentation.composables.showInterstitial
import com.lazydeveloper.trelloplex.presentation.composables.showInterstitial2
import com.lazydeveloper.trelloplex.ui.theme.Background_Black_10
import com.lazydeveloper.trelloplex.ui.theme.Background_Black_70
import com.lazydeveloper.trelloplex.ui.theme.Loading_Orange
import com.lazydeveloper.trelloplex.util.CommonEnum
import com.lazydeveloper.trelloplex.util.getSharedPrefs
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    val isEmpty = mPref.getString(CommonEnum.VERSION.toString(), "")
//    val viewModel: HomeScreenViewModel = viewModel()
    val networkState by viewModel.stateFlow.collectAsState()
    val trendingAllNetworkState by viewModel.trendingAllFlow.collectAsState()
    val trendingMovieNetworkState by viewModel.trendingMovieFlow.collectAsState()
    val trendingSeriesNetworkState by viewModel.trendingSeriesFlow.collectAsState()
    val popularTvNetworkState by viewModel.popularTvFlow.collectAsState()
    val topRatedMovieNetworkState by viewModel.topRatedMovieFlow.collectAsState()

    var message by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    var response = FireStoreServer()

//    val networkState by viewModel.stateFlow.collectAsState()

    var isExitDialogVisible = rememberSaveable {
        mutableStateOf(false)
    }

    BackHandler { isExitDialogVisible.value = true }

    LaunchedEffect(Unit) {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        var version = pInfo.versionName
        Log.e("FirebaseInit", "onCreate: $version")
        try {
            val data = db.collection("data").get().await()
            data.map {
                response = it.toObject(FireStoreServer::class.java)
                message = response.updatemsg
            }
        } catch (e: Exception) {
            Log.e("FirebaseInit", "onCreate1: ${e.message}")
        }
    }
    CustomAlertDialog(
        showDialog = isExitDialogVisible,
        message = "Are you sure you want to close the application?",
        confirmText = "Yes",
        dismissText = "No",
        onConfirmClick = {
            showInterstitial(
                context,
                onAdDismissed = {
                    activity.finish()
                },
                onError = {
                    showInterstitial2(
                        context,
                        onAdDismissed = {
                            activity.finish()
                        },
                        onError = {
                            activity.finish()
                        }
                    )
                }
            )
        },
        onDismissClick = {
            isExitDialogVisible.value = false
        }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            if(message != "")
                Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            ) {
                CustomText(
                    text = message,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Background_Black_10,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.CenterStart)
                        .height(25.dp)
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            animationMode = MarqueeAnimationMode.Immediately,
                            delayMillis = 0,
                            spacing = MarqueeSpacing(10.dp),
                            velocity = 30.dp
                        )
                        .clickable {
                            val uri: Uri =
                                Uri.parse(mPref.getString(CommonEnum.APP_URL.toString(), ""))
                            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                            // To count with Play market backstack, After pressing back button,
                            // to taken back to our application, we need to add following flags to intent.
                            goToMarket.addFlags(
                                Intent.FLAG_ACTIVITY_NO_HISTORY or
                                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                            )
                            try {
                                context.startActivity(goToMarket)
                            } catch (e: ActivityNotFoundException) {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(
                                            mPref.getString(
                                                CommonEnum.APP_URL.toString(),
                                                ""
                                            )
                                        )
                                    )
                                )
                            }
                        }
                )
            }
        }

        //Slider
        item {
            trendingAllNetworkState.let {
                when (it) {
                    is Resource.Loading -> {}

                    is Resource.Success -> {
                        val movie = it.data
                        Box(
                            modifier = Modifier
                                .wrapContentHeight()
//                                .fillMaxWidth()
//                                .wrapContentHeight()
//                                .padding(top = 20.dp)
                                .background(Color.Transparent)
                        ) {
                            CustomImageCarousel(movie, navController)
                        }
                    }

                    is Resource.Error -> {}

                    else -> {}
                }
            }

        }

        //Trending Rated Movies
        item {
            trendingMovieNetworkState.let {
                when (it) {
                    is Resource.Loading -> {}

                    is Resource.Success -> {
                        Header(
                            title = "Trending Movies",
                            showViewAll = true,
                            onClickViewAll = {
                                navController.navigate(
                                    Screen.MovieScreen.passArguments(0)
                                ) {
                                    navController.navigate(
                                        Screen.MovieScreen.passArguments(0)
                                    ) {
                                        popUpTo(Screen.HomeScreen.route) {
                                            inclusive = false
                                        }
                                    }
                                }
                            }
                        )

                        FetchData(
                            results = it.data.results,
                            isEmpty = isEmpty,
                            mPref = mPref,
                            navController = navController
                        )
                    }

                    is Resource.Error -> {}

                    else -> {}
                }
            }
        }

        //Popular Movies
        item {
            networkState.let {
                when (it) {
                    is Resource.Loading -> { LinearProgressIndicator() }

                    is Resource.Success -> {
                        Header(
                            title = "Popular Movies",
                            showViewAll = true,
                            onClickViewAll = {
                                navController.navigate(
                                    Screen.MovieScreen.passArguments(1)
                                ) {
                                    navController.navigate(
                                        Screen.MovieScreen.passArguments(1)
                                    ) {
                                        popUpTo(Screen.HomeScreen.route) {
                                            inclusive = false
                                        }
                                    }
                                }
                            }
                        )

                        FetchData(
                            results = it.data.results,
                            isEmpty = isEmpty,
                            mPref = mPref,
                            navController = navController
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

                    else -> {}
                }
            }
        }

        //Trending Rated Movies
        item {
            trendingSeriesNetworkState.let {
                when (it) {
                    is Resource.Loading -> {}

                    is Resource.Success -> {
                        val movie = it.data

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CustomText(
                                text = "Trending TV Series",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                            )
                            CustomText(
                                text = "View all",
                                fontSize = 16.sp,
                                color = Loading_Orange,
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .clickable {
                                        navController.navigate(Screen.SeriesScreen.passArguments(0))
                                    }
                            )

                        }
                        LazyRow(
                            modifier = Modifier
                                .height(240.dp)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(10.dp),
                        ) {
                            movie.results?.forEach {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(280.dp)
                                            .background(Color.Transparent)
                                            .padding(end = 10.dp)
                                    ) {
                                        Box(modifier = Modifier
                                            .width(130.dp)
                                            .height(180.dp)
                                            .clickable {
                                                navController.navigate(
                                                    Screen.SeriesDetailsScreen.passArguments(
                                                        it.id ?: 0
                                                    )
                                                )
                                            }
                                        ) {
                                            if(isEmpty != ""){
                                                CustomImage(
                                                    imageId =  R.drawable.ic_logo,
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .fillMaxSize(),
                                                    contentDescription = "logo",
                                                )
                                            }else{
                                                CustomImageAsync(
                                                    imageUrl = "${mPref.getString(CommonEnum.TMDB_IMAGE_PATH.toString(),"")}${it.posterPath}",
                                                    size = 512,
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .fillMaxSize(),
                                                    contentScale = ContentScale.FillBounds,
                                                    contentDescription = "ImageRequest example",
                                                )
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .width(35.dp)
                                                    .height(25.dp)
                                                    .padding(start = 5.dp, top = 5.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .align(Alignment.TopStart)
                                                    .background(Background_Black_70)
                                            )
                                            {
                                                CustomText(
                                                    text = "HD",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color.White,
                                                    modifier = Modifier
                                                        .align(Alignment.Center)
                                                )
                                            }
                                        }
                                        if(isEmpty != ""){
                                            CustomText(
                                                text = stringResource(id = R.string.app_name),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.White,
                                                maxLines = 2,
                                                overFlow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .width(130.dp)
                                                    .padding(start = 6.dp, top = 5.dp, end = 6.dp)
                                                    .align(Alignment.Start)
                                            )
                                        }else{

                                            CustomText(
                                                text = it?.name ?: "Error",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.White,
                                                maxLines = 2,
                                                overFlow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .width(130.dp)
                                                    .padding(start = 6.dp, top = 5.dp, end = 6.dp)
                                                    .align(Alignment.Start)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is Resource.Error -> {}

                    else -> {}
                }
            }
        }

        //Popular Tv Series
        item {
            popularTvNetworkState.let {
                when (it) {
                    is Resource.Loading -> {}

                    is Resource.Success -> {
                        val movie = it.data

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CustomText(
                                text = "Popular TV Series",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                            )
                            CustomText(
                                text = "View all",
                                fontSize = 16.sp,
                                color = Loading_Orange,
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .clickable {
                                        navController.navigate(Screen.SeriesScreen.passArguments(1))
                                    }
                            )

                        }
                        LazyRow(
                            modifier = Modifier
                                .height(240.dp)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(10.dp),
                        ) {
                            movie.results?.forEach {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(280.dp)
                                            .padding(end = 10.dp)
                                    ) {
                                        Box(modifier = Modifier
                                            .width(130.dp)
                                            .height(180.dp)
                                            .background(Color.Transparent)
                                            .clickable {
                                                navController.navigate(
                                                    Screen.SeriesDetailsScreen.passArguments(
                                                        it?.id ?: 0
                                                    )
                                                )
                                            }
                                        ) {
                                            if(isEmpty != ""){
                                                CustomImage(
                                                    imageId =  R.drawable.ic_logo,
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .fillMaxSize(),
                                                    contentScale = ContentScale.FillBounds,
                                                    contentDescription = "logo",
                                                )
                                            }else{
                                                CustomImageAsync(
                                                    imageUrl = "${mPref.getString(CommonEnum.TMDB_IMAGE_PATH.toString(),"")}${it?.posterPath}",
                                                    size = 512,
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .fillMaxSize(),
                                                    contentScale = ContentScale.FillBounds,
                                                    contentDescription = "poster",
                                                )
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .width(35.dp)
                                                    .height(25.dp)
                                                    .padding(start = 5.dp, top = 5.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .align(Alignment.TopStart)
                                                    .background(Background_Black_70)
                                            )
                                            {
                                                CustomText(
                                                    text = "HD",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color.White,
                                                    modifier = Modifier
                                                        .align(Alignment.Center)
                                                )
                                            }
                                        }
                                        if(isEmpty != ""){
                                            CustomText(
                                                text = stringResource(id = R.string.app_name),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.White,
                                                maxLines = 2,
                                                overFlow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .width(130.dp)
                                                    .padding(start = 6.dp, top = 5.dp, end = 6.dp)
                                                    .align(Alignment.Start)
                                            )
                                        }else{

                                            CustomText(
                                                text = it?.name ?: "Error",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.White,
                                                maxLines = 2,
                                                overFlow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .width(130.dp)
                                                    .padding(start = 6.dp, top = 5.dp, end = 6.dp)
                                                    .align(Alignment.Start)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is Resource.Error -> {}

                    else -> {}
                }
            }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    HomeScreen(navController = NavController(LocalContext.current))
}

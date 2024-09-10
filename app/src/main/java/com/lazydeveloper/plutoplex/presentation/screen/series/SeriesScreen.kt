package com.lazydeveloper.plutoplex.presentation.screen.series

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.lazydeveloper.plutoplex.R
import com.lazydeveloper.network.model.PopularTvResponse
import com.lazydeveloper.network.model.TrendingSeriesResponse
import com.lazydeveloper.plutoplex.navigation.Screen
import com.lazydeveloper.plutoplex.presentation.composables.CustomImage
import com.lazydeveloper.plutoplex.presentation.composables.CustomImageAsync
import com.lazydeveloper.plutoplex.presentation.composables.CustomText
import com.lazydeveloper.plutoplex.ui.theme.Background_Black_70
import com.lazydeveloper.plutoplex.ui.theme.Loading_Orange
import com.lazydeveloper.plutoplex.util.CommonEnum
import com.lazydeveloper.plutoplex.util.getSharedPrefs
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeriesScreen(
    navController: NavController,
    id: Int,
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (id == 0) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(0)
            }
        } else {
            coroutineScope.launch {
                pagerState.animateScrollToPage(1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(pagerState.pageCount) { index ->
                    CustomText(
                        text = getSeriesPageTitle(index),
                        color = if (pagerState.currentPage == index) Loading_Orange else Color(0xFF7A7A7A),
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
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
            CustomText(
                text = "Filter",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navController.navigate(
                            Screen.FilterScreen.passArguments(
                                1
                            )
                        )
                    }
            )
        }

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> TrendingSeriesContent(navController)

                1 -> PopularSeriesContent(navController)
            }
        }
    }
}

@Composable
fun SeriesItem(it: PopularTvResponse.Result, navController: NavController) {
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    val isEmpty = mPref.getString(CommonEnum.VERSION.toString(), "")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(Color.Transparent)
            .padding(8.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
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
                    imageId = R.drawable.ic_logo,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxSize(),
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
                    .padding(start = 10.dp, top = 5.dp, end = 10.dp)
            )
        }else{
            CustomText(
                text = it.name ?: "Error",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 2,
                overFlow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .width(130.dp)
                    .padding(start = 10.dp, top = 5.dp, end = 10.dp)
            )
        }
    }
}

@Composable
fun SeriesTrendingItem(
    it: TrendingSeriesResponse.Result,
    navController: NavController
) {
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    val isEmpty = mPref.getString(CommonEnum.VERSION.toString(), "")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(Color.Transparent)
            .padding(8.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
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
                    imageId = R.drawable.ic_logo,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxSize(),
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
                    .padding(start = 10.dp, top = 5.dp, end = 10.dp)
            )
        }else{
            CustomText(
                text = it.name ?: "Error",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 2,
                overFlow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .width(130.dp)
                    .padding(start = 10.dp, top = 5.dp, end = 10.dp)
            )
        }

    }
}


@Composable
fun getSeriesPageTitle(page: Int): String {
    return when (page) {
        0 -> "Trending"
        1 -> "Popular"
        else -> "Unknown"
    }
}

@Composable
fun TrendingSeriesContent(
    navController: NavController,
    viewModel: SeriesScreenViewModel = hiltViewModel()
) {
    val lazyMovieItems = viewModel.trendingSeriesFlow.collectAsLazyPagingItems()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(0.dp),
        ) {
            items(lazyMovieItems.itemCount) { item ->
                SeriesTrendingItem(it = lazyMovieItems[item]!!, navController = navController)
            }
            lazyMovieItems.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        Log.e("TAG", "MovieScreen: Loading")
                        //You can add modifier to manage load state when first time response page is loading
                    }

                    loadState.append is LoadState.Loading -> {
                        Log.e("TAG", "MovieScreen: Loading2")
                        //You can add modifier to manage load state when next response page is loading
                    }

                    loadState.append is LoadState.Error -> {
                        Log.e("TAG", "MovieScreen: Error")
                        //You can use modifier to show error message
                    }
                }
            }
        }
    }
}

@Composable
fun PopularSeriesContent(
    navController: NavController,
    viewModel: SeriesScreenViewModel = hiltViewModel()
) {
    val lazyMovieItems = viewModel.popularSeriesFlow.collectAsLazyPagingItems()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(0.dp),
        ) {
            items(lazyMovieItems.itemCount) { item ->
                SeriesItem(it = lazyMovieItems[item]!!, navController = navController)
            }
            lazyMovieItems.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        Log.e("TAG", "MovieScreen: Loading")
                        //You can add modifier to manage load state when first time response page is loading
                    }

                    loadState.append is LoadState.Loading -> {
                        Log.e("TAG", "MovieScreen: Loading2")
                        //You can add modifier to manage load state when next response page is loading
                    }

                    loadState.append is LoadState.Error -> {
                        Log.e("TAG", "MovieScreen: Error")
                        //You can use modifier to show error message
                    }
                }
            }
        }
    }
}

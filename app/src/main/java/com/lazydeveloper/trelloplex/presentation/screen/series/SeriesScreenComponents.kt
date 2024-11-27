package com.lazydeveloper.trelloplex.presentation.screen.series

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.lazydeveloper.network.model.PopularTvResponse
import com.lazydeveloper.network.model.TrendingSeriesResponse
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.trelloplex.navigation.Screen
import com.lazydeveloper.trelloplex.presentation.composables.CustomImage
import com.lazydeveloper.trelloplex.presentation.composables.CustomImageAsync
import com.lazydeveloper.trelloplex.presentation.composables.CustomText
import com.lazydeveloper.trelloplex.ui.theme.Background_Black_70
import com.lazydeveloper.trelloplex.util.CommonEnum
import com.lazydeveloper.trelloplex.util.getSharedPrefs

@Composable
fun TrendingSeriesContent(
    navController: NavController,
    viewModel: SeriesScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
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
                SeriesTrendingItem(
                    it = lazyMovieItems[item]!!,
                    isEmpty = mPref.getString(CommonEnum.VERSION.toString(), ""),
                    tmdImagePath = mPref.getString(CommonEnum.TMDB_IMAGE_PATH.toString(),""),
                    onItemClick = { id ->
                        navController.navigate(
                            Screen.SeriesDetailsScreen.passArguments(
                                id
                            )
                        )
                    }
                )
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
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
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
                SeriesItem(
                    it = lazyMovieItems[item]!!,
                    isEmpty = mPref.getString(CommonEnum.VERSION.toString(), ""),
                    tmdImagePath = mPref.getString(CommonEnum.TMDB_IMAGE_PATH.toString(),""),
                    onItemClick = { id ->
                        navController.navigate(
                            Screen.SeriesDetailsScreen.passArguments(
                                id
                            )
                        )
                    }
                )
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

@Preview(showBackground = false)
@Composable
fun SeriesItem(
    it: PopularTvResponse.Result? = null,
    isEmpty: String? = "",
    tmdImagePath: String? = "",
    onItemClick: (Int) -> Unit = {}
) {
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
                onItemClick(it?.id ?: 0)
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
                    imageUrl = "$tmdImagePath${it?.posterPath}",
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
                text = it?.name ?: "Error",
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

@Preview(showBackground = false)
@Composable
fun SeriesTrendingItem(
    it: TrendingSeriesResponse.Result? = null,
    isEmpty: String? = "",
    tmdImagePath: String? = "",
    onItemClick: (Int) -> Unit = {}
) {
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
                onItemClick(it?.id ?: 0)
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
                    imageUrl = "$tmdImagePath${it?.posterPath}",
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
                text = it?.name ?: "Error",
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
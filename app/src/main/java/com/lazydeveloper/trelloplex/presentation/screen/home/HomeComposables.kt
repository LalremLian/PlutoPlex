package com.lazydeveloper.trelloplex.presentation.screen.home

import android.content.SharedPreferences
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lazydeveloper.network.model.global.Result
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.trelloplex.navigation.Screen
import com.lazydeveloper.trelloplex.presentation.composables.CustomImage
import com.lazydeveloper.trelloplex.presentation.composables.CustomImageAsync
import com.lazydeveloper.trelloplex.presentation.composables.CustomText
import com.lazydeveloper.trelloplex.ui.theme.Background_Black_70
import com.lazydeveloper.trelloplex.ui.theme.Loading_Orange
import com.lazydeveloper.trelloplex.util.CommonEnum
import com.lazydeveloper.trelloplex.util.movieList


@Composable
fun LinearProgressIndicator(){
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        androidx.compose.material.LinearProgressIndicator(
            color = Loading_Orange,
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = false)
@Composable
fun Header(
    title: String = "Title",
    showViewAll: Boolean = true,
    onClickViewAll: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CustomText(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier
                .padding(start = 16.dp)
        )
        if(!showViewAll) return
        CustomText(
            text = "View all",
            fontSize = 16.sp,
            color = Loading_Orange,
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable { onClickViewAll.invoke() }
        )
    }
}


@Preview(showBackground = false)
@Composable
fun FetchData(
    results: List<Result>? = movieList,
    isEmpty: String? = "",
    viewModel: HomeScreenViewModel? = null,
    navController: NavController? = null,
) {
    LazyRow(
        modifier = Modifier
            .height(240.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(10.dp),
    ) {
        results?.forEach {
            item {
                MovieItem(
                    it = it,
                    isEmpty = isEmpty,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}


@Preview(showBackground = false)
@Composable
fun MovieItem(
    it: Result? = null,
    isEmpty: String? = "",
    viewModel: HomeScreenViewModel? = null,
    navController: NavController? = null,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(Color.Transparent)
            .padding(end = 10.dp)
    ) {
        Box(modifier = Modifier
            .width(135.dp)
            .height(180.dp)
            .clickable {
                navController?.navigate(
                    Screen.MovieDetailsScreen.passArguments(
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
                    contentDescription = "ImageRequest example",
                )
            }else{
                CustomImageAsync(
                    imageUrl = "${viewModel?.mPref?.tmdbImagePath}${it?.posterPath}",
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
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
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
                text = it?.title ?: "No Title",
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
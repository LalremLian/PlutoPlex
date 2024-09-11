package com.lazydeveloper.trelloplex.presentation.screen.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.database.entities.HistoryEntity
import com.lazydeveloper.trelloplex.navigation.Screen
import com.lazydeveloper.trelloplex.presentation.composables.CustomImage
import com.lazydeveloper.trelloplex.presentation.composables.CustomImageAsync
import com.lazydeveloper.trelloplex.presentation.composables.CustomText
import com.lazydeveloper.trelloplex.ui.theme.Background_Black
import com.lazydeveloper.trelloplex.ui.theme.Loading_Orange
import com.lazydeveloper.trelloplex.util.CommonEnum
import com.lazydeveloper.trelloplex.util.getSharedPrefs
import java.util.Locale

@Composable
fun HistoryScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<HistoryScreenViewModel>()
    val isExitDialogVisible = rememberSaveable { mutableStateOf(false) }

    if (isExitDialogVisible.value)
        AlertDialog(
            onDismissRequest = { isExitDialogVisible.value = false },
            text = {
                CustomText(
                    "Are you sure you want to clear all history?",
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                )
            },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    CustomText(
                        text = "Cancel",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier
                            .clickable {
                                isExitDialogVisible.value = false
                            }
                            .padding(16.dp)
                    )
                    CustomText(
                        text = "Clear",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier
                            .clickable {
                                viewModel.deleteAllHistory()
                                isExitDialogVisible.value = false
                            }
                            .padding(16.dp)
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                }
            },
            backgroundColor = Background_Black,
        )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomImage(
                    imageId = R.drawable.ic_back,
                    modifier = Modifier
                        .size(55.dp)
                        .padding(16.dp)
                        .clickable { navController.popBackStack() },
                    contentDescription = "back"
                )
                CustomText(
                    text = "History",
                    modifier = Modifier
                        .clickable { navController.popBackStack() },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.White,
                )
            }
            CustomText(
                text = "Clear All",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        isExitDialogVisible.value = true
                    },
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                color = Loading_Orange,
            )
        }
        HistoryScreenBody(navController = navController)
    }
}

@Composable
fun HistoryScreenBody(
    navController: NavController,
) {
    val viewModel = hiltViewModel<HistoryScreenViewModel>()
    viewModel.getAllHistory()
    val searchHistoryListState = viewModel.searchHistoryListFlow2.collectAsState()

    if (searchHistoryListState.value.isEmpty()) {
        CustomText(
            "No history found",
            modifier = Modifier
                .padding(top = 280.dp)
                .fillMaxSize(),
            textAlign = TextAlign.Center,
            color = Color.White,
        )
    } else {
        LazyColumn {
            items(searchHistoryListState.value.size) { index ->
                val searchHistory = searchHistoryListState.value[index]
                HistoryItem(navController = navController, searchHistory = searchHistory)
            }
        }
    }
}

@Composable
fun HistoryItem(
    navController: NavController,
    searchHistory: HistoryEntity,
) {
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    val isEmpty = mPref.getString(CommonEnum.VERSION.toString(), "")
    val gson = Gson()
    val type = object : TypeToken<List<String>>() {}.type
    val newList: List<String> = gson.fromJson(searchHistory.genre, type)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    if (searchHistory.type == "movie") {
                        navController.navigate(
                            Screen.MovieDetailsScreen.passArguments(
                                searchHistory.movieId.toInt()
                            )
                        )
                    } else {
                        navController.navigate(
                            Screen.SeriesDetailsScreen.passArguments(
                                searchHistory.movieId.toInt()
                            )
                        )
                    }
                }
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                if (isEmpty != "") {
                    CustomImage(
                        imageId = R.drawable.ic_logo,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .width(80.dp)
                            .height(120.dp),
                        contentDescription = "logo",
                    )
                } else {
                    CustomImageAsync(
                        imageUrl = "${mPref.getString(CommonEnum.TMDB_IMAGE_PATH.toString(),"")}${searchHistory.poster}",
                        size = 512,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .width(80.dp)
                            .height(120.dp),
                        contentScale = ContentScale.Crop,
                        contentDescription = "poster",
                    )
                }
                Column {
                    CustomText(
                        text = searchHistory.movieName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overFlow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 16.dp)
                            .align(Alignment.Start)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        CustomText(
                            text = searchHistory.type.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            },
                            fontSize = 14.sp,
                            color = Color(0xFFBDBBBB),
                            modifier = Modifier
                                .padding(start = 8.dp, top = 5.dp, end = 5.dp)
                        )
                        CustomText(
                            text = "• ${searchHistory.year} •",
                            fontSize = 14.sp,
                            color = Color(0xFFBDBBBB),
                            modifier = Modifier
                                .padding(top = 5.dp, end = 5.dp)
                        )
                        CustomText(
                            text = searchHistory.region,
                            fontSize = 14.sp,
                            color = Color(0xFFBDBBBB),
                            modifier = Modifier
                                .padding(top = 5.dp, end = 5.dp)
                        )
                    }
                    Row {
                        CustomText(
                            text = newList.joinToString(separator = ", "),
                            color = Color(0xFFBDBBBB),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 5.dp, end = 8.dp)
                        )
                    }
                }
            }
        }
        CustomText(
            text = searchHistory.watchDate,
            fontSize = 10.sp,
            color = Color(0xFFBDBBBB),
            modifier = Modifier
                .padding(top = 5.dp, end = 16.dp, bottom = 5.dp)
                .align(Alignment.BottomEnd)
        )
    }
}
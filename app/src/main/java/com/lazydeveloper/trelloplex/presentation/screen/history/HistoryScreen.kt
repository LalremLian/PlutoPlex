package com.lazydeveloper.trelloplex.presentation.screen.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lazydeveloper.trelloplex.navigation.Screen
import com.lazydeveloper.trelloplex.presentation.composables.CustomText

@Composable
fun HistoryScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<HistoryScreenViewModel>()
    val isExitDialogVisible = rememberSaveable { mutableStateOf(false) }

    ClearHistoryDialog(
        isExitDialogVisible = isExitDialogVisible,
        onCancelClick = {
            isExitDialogVisible.value = false
        },
        onClearClick = {
            viewModel.deleteAllHistory()
            isExitDialogVisible.value = false
        }
    )

    Column {
        HistoryTopBar(
            onBackClick = {
                navController.popBackStack()
            },
            onClearAllClick = {
                isExitDialogVisible.value = true
            }
        )
        HistoryScreenBody(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun HistoryScreenBody(
    navController: NavController,
    viewModel: HistoryScreenViewModel? = null
) {
    val searchHistoryListState = viewModel?.searchHistoryListFlow2?.collectAsState()

    if (searchHistoryListState?.value?.isEmpty() == true) {
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
            items(searchHistoryListState?.value?.size!!) { index ->
                val searchHistory = searchHistoryListState.value[index]
                HistoryItem(
                    searchHistory = searchHistory,
                    isEmpty = viewModel.mPref.appVersion,
                    imageBaseUrl = viewModel.mPref.tmdbImagePath,
                    onItemClick = {
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
                )
            }
        }
    }
}


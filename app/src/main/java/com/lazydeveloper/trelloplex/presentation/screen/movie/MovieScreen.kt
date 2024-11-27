package com.lazydeveloper.trelloplex.presentation.screen.movie

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lazydeveloper.trelloplex.navigation.Screen
import com.lazydeveloper.trelloplex.presentation.composables.CustomText
import com.lazydeveloper.trelloplex.ui.theme.Loading_Orange
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieScreen(
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
                        text = getMoviePageTitle(index),
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
                                0
                            )
                        )
                    }
            )
        }

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> TrendingContent(navController)

                1 -> PopularContent(navController)
            }
        }
    }
}




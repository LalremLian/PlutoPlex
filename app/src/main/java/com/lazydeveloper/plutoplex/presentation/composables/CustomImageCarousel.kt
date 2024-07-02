package com.lazydeveloper.plutoplex.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import com.lazydeveloper.plutoplex.R
import com.lazydeveloper.network.model.TrendingAllResponse
import com.lazydeveloper.plutoplex.navigation.Screen
import com.lazydeveloper.plutoplex.util.CommonEnum
import com.lazydeveloper.plutoplex.util.getSharedPrefs
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomImageCarousel(
    sliderList: TrendingAllResponse,
    navController: NavController,
    onGlobalPositionedSize: ((selectedPosition: Int, carouselHeight: Float) -> Unit)? = null
) {
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    val isEmpty = mPref.getString(CommonEnum.VERSION.toString(), "")

    val pagerState = rememberPagerState(
        initialPage = 3,
        pageCount = { sliderList.results?.size ?: 0 }
    )

    Column(modifier = Modifier.wrapContentSize()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 120.dp),
            pageSpacing = 5.dp,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    onGlobalPositionedSize?.invoke(
                        pagerState.currentPage % sliderList.results?.size!!,
                        it.size.height.toFloat()
                    )
                }
        ) { index ->
            Card(
                border = BorderStroke(1.dp, Color(0xFFB3B3B3)),
                modifier = Modifier
                    .padding(4.dp)
                    .height(210.dp)
                    .width(210.dp)
                    .clickable {
                        if (sliderList.results?.get(index)?.mediaType == "tv") {
                            navController.navigate(
                                Screen.SeriesDetailsScreen.passArguments(
                                    sliderList.results!![index].id ?: 0
                                )
                            )
                        } else {
                            navController.navigate(
                                Screen.MovieDetailsScreen.passArguments(
                                    sliderList.results?.get(index)?.id ?: 0
                                )
                            )
                        }
                    }
                    .graphicsLayer {
                        val pageOffset =
                            (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
                        lerp(
                            start = 0.9f,
                            stop = 1f,
                            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                        ).also { scale ->
                            scaleX = scale * 1.1f
                            scaleY = scale * 1.2f
                        }
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                        )
                    }
            ) {
                Box {
                    if (isEmpty != "") {
                        Image(
                            painterResource(id = R.drawable.ic_logo),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentScale = ContentScale.FillHeight,
                            contentDescription = "ImageRequest example",
                        )
                    } else {
                        CustomImageAsync(
                            imageUrl = "${
                                mPref.getString(
                                    CommonEnum.TMDB_IMAGE_PATH.toString(),
                                    ""
                                )
                            }${sliderList.results?.get(index)?.posterPath}",
                            size = 512,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "ImageRequest example",
                        )
                    }
                }
            }
        }

        if (isEmpty != "") {
            Text(
                text = stringResource(id = R.string.app_name),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 30.dp, start = 16.dp, end = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            Text(
                text = sliderList.results?.get(pagerState.currentPage)?.title
                    ?: sliderList.results?.get(pagerState.currentPage)?.name!!,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 30.dp, start = 16.dp, end = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(id = R.drawable.ic_star),
                contentDescription = "Back",
                modifier = Modifier
                    .size(16.dp)
                    .padding(top = 4.dp),
            )
            Text(
                text = sliderList.results?.get(pagerState.currentPage)?.voteAverage?.toString()
                    ?.substring(0, 3)
                    ?: "",
                color = Color(0xFFB3B3B3),
                fontSize = 14.sp
            )
            Text(
                text = " • (${sliderList.results?.get(pagerState.currentPage)?.voteCount?.toString()} votes)",
                color = Color(0xFFB3B3B3),
                fontSize = 14.sp
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        repeat(
            times = Int.MAX_VALUE,
            action = {
                delay(4000)
                try {
                    val nextPage = (pagerState.currentPage + 1) % (sliderList.results?.size ?: 0)
                    pagerState.animateScrollToPage(page = nextPage)
                } catch (exp: Exception) {
                    exp.printStackTrace()
                }
            }
        )
    }
}
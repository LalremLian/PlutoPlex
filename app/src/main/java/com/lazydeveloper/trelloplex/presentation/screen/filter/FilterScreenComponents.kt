package com.lazydeveloper.trelloplex.presentation.screen.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazydeveloper.network.model.MovieFilterResponse
import com.lazydeveloper.network.model.TvFilterResponse
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.trelloplex.presentation.composables.CustomImage
import com.lazydeveloper.trelloplex.presentation.composables.CustomImageAsync
import com.lazydeveloper.trelloplex.presentation.composables.CustomText
import com.lazydeveloper.trelloplex.ui.theme.Background_Black_70
import com.lazydeveloper.trelloplex.util.CommonEnum
import com.lazydeveloper.trelloplex.util.getSharedPrefs

@Preview(showBackground = false)
@Composable
fun FilterMovieItem(
    it: MovieFilterResponse.Result? = null,
    onItemClick: (Int) -> Unit = {}
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
            .clickable { onItemClick(it?.id ?: 0) }
        ) {
            if (isEmpty != "") {
                CustomImage(
                    imageId = R.drawable.ic_logo,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxSize(),
                    contentDescription = "ImageRequest example",
                )
            } else {
                CustomImageAsync(
                    imageUrl = "${
                        mPref.getString(
                            CommonEnum.TMDB_IMAGE_PATH.toString(),
                            ""
                        )
                    }${it?.posterPath}",
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
        CustomText(
            text = it?.title ?: "Error",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 2,
            overFlow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(130.dp)
                .padding(start = 5.dp, top = 5.dp, end = 5.dp)
                .align(Alignment.Start)
        )
    }
}

@Preview(showBackground = false)
@Composable
fun FilterTvItem(
    it: TvFilterResponse.Result? = null,
    onItemClick: (Int) -> Unit = {},
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
            .clickable { onItemClick(it?.id ?: 0) }
        ) {
            if (isEmpty != "") {
                CustomImage(
                    imageId = R.drawable.ic_logo,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxSize(),
                    contentDescription = "ImageRequest example",
                )
            } else {
                CustomImageAsync(
                    imageUrl = "${
                        mPref.getString(
                            CommonEnum.TMDB_IMAGE_PATH.toString(),
                            ""
                        )
                    }${it?.posterPath}",
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
        CustomText(
            text = it?.name ?: "Error",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 2,
            overFlow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(130.dp)
                .padding(start = 5.dp, top = 5.dp, end = 5.dp)
                .align(Alignment.Start)
        )
    }
}
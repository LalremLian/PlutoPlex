package com.lazydeveloper.plutoplex.presentation.screen.copyright

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazydeveloper.plutoplex.R
import com.lazydeveloper.plutoplex.presentation.composables.CustomImage
import com.lazydeveloper.plutoplex.presentation.composables.CustomText
import com.lazydeveloper.plutoplex.util.verrse_one

@Preview
@Composable
fun CopyrightScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
    ) {
        CustomImage(
            imageId = R.drawable.ic_tmdb,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp, top = 16.dp),
            contentDescription = ""
        )
        CustomText(
            text = verrse_one,
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Start,
        )
    }
}
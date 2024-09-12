package com.lazydeveloper.trelloplex.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.trelloplex.ui.theme.padding

@SuppressLint("ComposableNaming")
@Preview(
    showBackground = true,
    backgroundColor = 0xFFFF5FFF
)
@Composable
fun CustomItem(
    text: String = "Title",
    icon: Int = R.drawable.ic_movie,
    nextIcon: Int = R.drawable.ic_next,
    isOpen: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(modifier = Modifier
        .clickable { onClick.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = padding.mediumLarge,
                    end = padding.mediumLarge,
                    top = padding.small,
                    bottom = padding.small
                ),
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = icon), contentDescription = "cast",
                    Modifier.size(padding.medium)
                )
                CustomText(
                    text = text,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = padding.small)
                )
            }
            Image(
                painter = painterResource(id = nextIcon),
                contentDescription = "",
            )
        }
        if (!isOpen) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(padding.doubleExtraSmall)
                    .padding(start = padding.medium, end = padding.medium)
            ) {
                val lineStart = Offset(0f, size.height)
                val lineEnd = Offset(size.width, size.height)
                drawLine(color = Color.Gray, start = lineStart, end = lineEnd)
            }
        }
    }
}
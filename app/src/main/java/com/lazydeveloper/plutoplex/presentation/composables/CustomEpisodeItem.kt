package com.lazydeveloper.plutoplex.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazydeveloper.plutoplex.R
import com.lazydeveloper.network.model.SeriesDetailsResponse
import com.lazydeveloper.plutoplex.util.getSharedPrefs
import com.lazydeveloper.plutoplex.util.putInt

@Composable
fun CustomEpisodeItem(
    episode: Int,
    model: SeriesDetailsResponse,
    onClick: () -> Unit
):Int {
    val context = androidx.compose.ui.platform.LocalContext.current
    val sharedPreferences = context.getSharedPrefs()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
                sharedPreferences.putInt("my_key", episode+1) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(100.dp)
                .padding(8.dp)
                .background(Color(0xFF3F3E3E))

        ) {
            Image(
                painterResource(id = R.drawable.ic_movie), contentDescription = "Back",
                modifier = Modifier
                    .size(85.dp)
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(text = "Episode ${episode + 1}",
                style = TextStyle(color = Color.White),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(text = "Released - ${model.firstAirDate}",
                style = TextStyle(color = Color(0xFF8F8F8F)),
                fontSize = 14.sp,)
        }
    }
    return episode
}
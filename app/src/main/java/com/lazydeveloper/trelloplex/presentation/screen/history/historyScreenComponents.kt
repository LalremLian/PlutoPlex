package com.lazydeveloper.trelloplex.presentation.screen.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lazydeveloper.database.entities.HistoryEntity
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.trelloplex.presentation.composables.CustomImage
import com.lazydeveloper.trelloplex.presentation.composables.CustomImageAsync
import com.lazydeveloper.trelloplex.presentation.composables.CustomText
import com.lazydeveloper.trelloplex.ui.theme.Background_Black
import com.lazydeveloper.trelloplex.ui.theme.Loading_Orange
import com.lazydeveloper.trelloplex.util.fromJsonToStringList
import java.util.Locale

@Preview(showBackground = false)
@Composable
fun HistoryTopBar(
    onBackClick: () -> Unit = {},
    onClearAllClick: () -> Unit = {}
){
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
                    .clickable { onBackClick.invoke() },
                contentDescription = "back"
            )
            CustomText(
                text = "History",
                modifier = Modifier
                    .clickable { onBackClick.invoke() },
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
                    onClearAllClick.invoke()
                },
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            color = Loading_Orange,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun HistoryItem(
    searchHistory: HistoryEntity? = null,
    onItemClick: (HistoryEntity) -> Unit = {},
    imageBaseUrl: String = "",
    isEmpty: String = "",
) {
    val newList: List<String> = searchHistory?.genre.fromJsonToStringList()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    onItemClick(searchHistory!!)
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
                        imageUrl = "${imageBaseUrl}${searchHistory?.poster}",
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
                        text = searchHistory?.movieName ?: "",
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
                        if (searchHistory != null) {
                            CustomText(
                                text = searchHistory.type.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.ROOT
                                    ) else it.toString()
                                },
                                fontSize = 12.sp,
                                color = Color(0xFFBDBBBB),
                                modifier = Modifier
                                    .padding(start = 8.dp, top = 5.dp, end = 5.dp)
                            )
                        }
                        CustomText(
                            text = "• ${searchHistory?.year} •",
                            fontSize = 12.sp,
                            color = Color(0xFFBDBBBB),
                            modifier = Modifier
                                .padding(top = 5.dp, end = 5.dp)
                        )
                        CustomText(
                            text = searchHistory?.region ?: "",
                            fontSize = 12.sp,
                            color = Color(0xFFBDBBBB),
                            modifier = Modifier
                                .padding(top = 5.dp, end = 5.dp)
                        )
                    }
                    Row {
                        CustomText(
                            text = newList.joinToString(separator = ", "),
                            color = Color(0xFFBDBBBB),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 3.dp, end = 8.dp)
                        )
                    }
                }
            }
        }
        CustomText(
            text = searchHistory?.watchDate ?: "",
            fontSize = 10.sp,
            color = Color(0xFFBDBBBB),
            modifier = Modifier
                .padding(top = 5.dp, end = 16.dp, bottom = 5.dp)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun ClearHistoryDialog(
    isExitDialogVisible: MutableState<Boolean>,
    onCancelClick: () -> Unit = {},
    onClearClick: () -> Unit = {}
) {
    if (isExitDialogVisible.value) {
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
                                onCancelClick.invoke()
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
                                onClearClick.invoke()
                            }
                            .padding(16.dp)
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                }
            },
            backgroundColor = Background_Black,
        )
    }
}
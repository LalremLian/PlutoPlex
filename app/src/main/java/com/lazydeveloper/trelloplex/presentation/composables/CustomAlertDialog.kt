package com.lazydeveloper.trelloplex.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazydeveloper.trelloplex.ui.theme.Background_Black

@Composable
fun CustomAlertDialog(
    showDialog: MutableState<Boolean>,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            text = {
                CustomText(
                    message,
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
                        text = dismissText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier
                            .clickable {
                                onDismissClick.invoke()
                                showDialog.value = false
                            }
                            .padding(16.dp)
                    )
                    CustomText(
                        text = confirmText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier
                            .clickable {
                                onConfirmClick.invoke()
                                showDialog.value = false
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
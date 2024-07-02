package com.lazydeveloper.plutoplex.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimen(
    val default: Dp = 0.dp,
    val doubleExtraSmall: Dp = 2.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val smallMedium: Dp = 12.dp,
    val medium: Dp = 16.dp,
    val mediumLarge: Dp = 24.dp,
    val large: Dp = 32.dp,
    val extraLarge: Dp = 64.dp,
    val doubleExtraLarge: Dp = 84.dp
)

val LocalPadding = compositionLocalOf { Dimen() }

val padding: Dimen
    @Composable
    @ReadOnlyComposable
    get() = LocalPadding.current
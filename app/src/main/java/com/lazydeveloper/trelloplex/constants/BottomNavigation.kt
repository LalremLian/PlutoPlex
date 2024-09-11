package com.lazydeveloper.trelloplex.constants

import androidx.annotation.DrawableRes
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.trelloplex.navigation.Screen

enum class BottomNavigation(
    @DrawableRes val icon: Int,
    val route: String,
    val title: String = ""
) {
    HOME(
        R.drawable.ic_home,
        Screen.HomeScreen.route,
        "Home"
    ),
    MOVIE(
        R.drawable.ic_movie,
        Screen.MovieScreen.passArguments(0),
        "Movies"
    ),
    SERIES(
        R.drawable.ic_tv,
        Screen.SeriesScreen.passArguments(0),
        "TV Series"
    ),
    MORE(
        R.drawable.ic_more,
        Screen.MoreScreen.route,
        "More"
    ),
}
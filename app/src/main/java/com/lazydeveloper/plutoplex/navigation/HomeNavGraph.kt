package com.lazydeveloper.plutoplex.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lazydeveloper.plutoplex.presentation.screen.copyright.CopyrightScreen
import com.lazydeveloper.plutoplex.presentation.screen.details.DetailsScreen
import com.lazydeveloper.plutoplex.presentation.screen.details.SeriesDetailsScreen
import com.lazydeveloper.plutoplex.presentation.screen.filter.FilterScreen
import com.lazydeveloper.plutoplex.presentation.screen.history.HistoryScreen
import com.lazydeveloper.plutoplex.presentation.screen.home.HomeScreen
import com.lazydeveloper.plutoplex.presentation.screen.media3.Media3Screen
import com.lazydeveloper.plutoplex.presentation.screen.more.MoreScreen
import com.lazydeveloper.plutoplex.presentation.screen.movie.MovieScreen
import com.lazydeveloper.plutoplex.presentation.screen.player.PlayerScreen
import com.lazydeveloper.plutoplex.presentation.screen.player.SeriesPlayerScreen
import com.lazydeveloper.plutoplex.presentation.screen.privacy.PrivacyScreen
import com.lazydeveloper.plutoplex.presentation.screen.search.SearchScreen
import com.lazydeveloper.plutoplex.presentation.screen.series.SeriesScreen

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun HomeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        route = Graph.HomeGraph.route,
        startDestination = Screen.HomeScreen.route,
        modifier = modifier,
        enterTransition = {
            fadeIn(tween(300))
        },
        exitTransition = {
            fadeOut(tween(300))
        },
        popEnterTransition = {
            fadeIn(tween(300))
        },
        popExitTransition = {
            fadeOut(tween(300))
        }
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(Screen.Media3Screen.route) { backStackEntry ->
            val bundle = backStackEntry.arguments?.getString(Screen.COMIC_ID_KEY)!!
            val movieTitle = backStackEntry.arguments?.getString(Screen.TITLE_ID_KEY)!!
            Media3Screen(
                url = bundle,
                title = movieTitle,
                navController = navController
            )
        }
        composable(Screen.PlayerScreen.route) { backStackEntry ->
            val bundle = backStackEntry.arguments?.getString(Screen.COMIC_ID_KEY)!!.toInt()
            val serverId = backStackEntry.arguments?.getString(Screen.SERVER_ID_KEY)!!.toInt()
            PlayerScreen(
                navController,
                id = bundle,
                serverId = serverId
            )
        }
        composable(Screen.SeriesPlayerScreen.route) { backStackEntry ->
            val bundle = backStackEntry.arguments?.getString(Screen.COMIC_ID_KEY)!!.toInt()
            val seasonId = backStackEntry.arguments?.getString(Screen.SEASON_ID_KEY)!!.toInt()
            val episodeId = backStackEntry.arguments?.getString(Screen.EPISODE_ID_KEY)!!.toInt()
            val serverId = backStackEntry.arguments?.getString(Screen.SERVER_ID_KEY)!!.toInt()
            SeriesPlayerScreen(
                navController,
                id = bundle,
                seasonId = seasonId,
                episodeId = episodeId,
                serverId = serverId
            )
        }
        composable(Screen.MovieScreen.route) { backStackEntry ->
            val bundle = backStackEntry.arguments?.getString(Screen.COMIC_ID_KEY)!!.toInt()
            MovieScreen(navController, id = bundle)
        }
        composable(Screen.SeriesScreen.route) {backStackEntry ->
            val bundle = backStackEntry.arguments?.getString(Screen.COMIC_ID_KEY)!!.toInt()
            SeriesScreen(navController, id = bundle)
        }
        composable(Screen.MoreScreen.route) {
            MoreScreen(navController)
        }
//        composable(Screen.DetailsScreen.route) {
////            val id = it.arguments?.getString(Screen.HomeScreen.route)!!.toInt()
//            DetailsScreen(navController)
//        }
        composable(Screen.MovieDetailsScreen.route) { backStackEntry ->
            val bundle = backStackEntry.arguments?.getString(Screen.COMIC_ID_KEY)!!.toInt()
            DetailsScreen(navController, id = bundle)
        }
        composable(Screen.SeriesDetailsScreen.route) { backStackEntry ->
            val bundle = backStackEntry.arguments?.getString(Screen.COMIC_ID_KEY)!!.toInt()
            SeriesDetailsScreen(navController, id = bundle)
        }
        composable(Screen.SearchScreen.route) {
            SearchScreen(navController)
        }
//        composable(Screen.BookmarksScreen.route) {
//            BookmarksScreen(navController)
//        }
//        composable(Screen.SearchScreen.route) {
//            SearchScreen(navController)
//        }
        composable(Screen.PrivacyPolicyScreen.route) {
            PrivacyScreen()
        }
        composable(Screen.CopyrightScreen.route) {
            CopyrightScreen()
        }
        composable(Screen.HistoryScreen.route) {
            HistoryScreen(navController)
        }
        composable(Screen.FilterScreen.route) {backStackEntry ->
            val bundle = backStackEntry.arguments?.getString(Screen.COMIC_ID_KEY)!!.toInt()
            FilterScreen(navController, id = bundle)
        }

//        composable(Screen.ProfileScreen.route) {
//            ProfileScreen(navController)
//        }
//        composable(Screen.ComicInfoScreen.route) {
//            ComicInfoScreen(navController)
//        }
//        composable(Screen.ComicReaderScreen.route) {
//            val id = it.arguments?.getString(Screen.COMIC_ID_KEY)!!.toInt()
//            ComicReaderScreen(navController, id)
//        }
    }
}
package com.lazydeveloper.trelloplex.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lazydeveloper.trelloplex.presentation.screen.main.MainScreen
import com.lazydeveloper.trelloplex.presentation.screen.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun RootNavGraph(
    navHostController: NavHostController,
) {
    NavHost(
        navController = navHostController,
        route = Graph.RootGraph.route,
        startDestination = Screen.SplashScreen.route,
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
    ){
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController = navHostController)
        }
        composable(Graph.HomeGraph.route) {
            MainScreen(navHostController = rememberNavController())
        }
//        composable(Graph.WebGraph.route) {
//            WebviewScreen()
//        }
    }

}
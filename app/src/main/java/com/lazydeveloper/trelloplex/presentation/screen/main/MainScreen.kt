package com.lazydeveloper.trelloplex.presentation.screen.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.trelloplex.navigation.HomeNavGraph
import com.lazydeveloper.trelloplex.navigation.Screen
import com.lazydeveloper.trelloplex.presentation.composables.CustomImage
import com.lazydeveloper.trelloplex.ui.theme.Background_Black

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navHostController: NavHostController,
) {
    val context = LocalContext.current
    val activity = context as Activity
    val scaffoldState = rememberScaffoldState()

    // Remember a SystemUiController
    val systemUiController = rememberSystemUiController()
    var currentPageState = remember {
        mutableStateOf(Screen.HomeScreen.route)
    }
    navHostController.addOnDestinationChangedListener { _, destination, _ ->
        currentPageState.value = destination.route.toString()
        Log.d("route_", "onDestinationChanged: route: ${currentPageState.value}")
    }

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons (preferred, as we're using a light status bar).
        systemUiController.setSystemBarsColor(
            color = Background_Black,
        )
        if (currentPageState.value == Screen.PlayerScreen.route
            || currentPageState.value == Screen.SeriesPlayerScreen.route
            || currentPageState.value == Screen.Media3Screen.route) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val controller = activity.window.decorView.windowInsetsController
                controller?.hide(WindowInsets.Type.statusBars())
            } else {
                @Suppress("DEPRECATION")
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            }
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val controller = activity.window.decorView.windowInsetsController
                controller?.show(WindowInsets.Type.statusBars())
            } else {
                @Suppress("DEPRECATION")
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            }
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = currentPageState.value == Screen.HomeScreen.route
                        || currentPageState.value == Screen.MoreScreen.route
                        || currentPageState.value == Screen.SeriesScreen.route
                        || currentPageState.value == Screen.MovieScreen.route,
                enter = slideInVertically(
                    initialOffsetY = { it / 2 }
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it / 2 }
                )
            ) {
                StandardBottomAppBar(
                    navController = navHostController,
                )
            }
        },
        scaffoldState = scaffoldState,
        backgroundColor = Background_Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Transparent)
        ) {
            AnimatedVisibility(
                visible = currentPageState.value == Screen.HomeScreen.route
                        || currentPageState.value == Screen.SeriesScreen.route
                        || currentPageState.value == Screen.MovieScreen.route,
                enter = slideInVertically(
                    initialOffsetY = { it / 2 }
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it / 10 }
                ))

            {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier
                                .wrapContentWidth()
                                .height(60.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
//                            CustomImage(
//                                imageId = R.drawable.ic_logo,
//                                contentDescription = "logo",
//                                modifier = Modifier
//                                    .height(40.dp)
//                                    .padding(2.dp)
//                            )
                            CustomImage(
                                imageId = R.drawable.ic_title,
                                contentDescription = null,
                                modifier = Modifier
                                    .height(28.dp)
                                    .padding(start = 0.dp)
                            )
                        }
                        CustomImage(
                            imageId = R.drawable.ic_search,
                            contentDescription = "search",
                            modifier = Modifier
                                .height(25.dp)
                                .padding(start = 2.dp)
                                .clickable { navHostController.navigate(Screen.SearchScreen.route) }
                        )
                    }
                }
            }

            HomeNavGraph(
                navController = navHostController,
            )
        }
    }
}

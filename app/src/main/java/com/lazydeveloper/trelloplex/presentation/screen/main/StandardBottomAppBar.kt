package com.lazydeveloper.trelloplex.presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lazydeveloper.trelloplex.constants.BottomNavigation
import com.lazydeveloper.trelloplex.presentation.composables.CustomImage
import com.lazydeveloper.trelloplex.presentation.composables.CustomText
import com.lazydeveloper.trelloplex.ui.theme.Background_Black
import com.lazydeveloper.trelloplex.ui.theme.Loading_Orange

@Composable
fun StandardBottomAppBar(
    navController: NavController,
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    BottomAppBar(
        backgroundColor = Background_Black
    ) {
        BottomNavigation.entries.forEach {
            BottomNavigationItem(
                label = {
                    CustomText(
                        text = it.title,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.W500
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == it.route } == true,
                onClick = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .background(
                                if (it.route.split("/")[0] == currentDestination?.route?.split("/")?.get(0)) Loading_Orange else Color.Transparent,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .width(45.dp),
                    ) {
                        CustomImage(
                            imageId = it.icon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(23.dp)
                                .padding(4.dp)
                                .align(Alignment.Center),
                        )
                    }
                }
            )
        }
    }
}
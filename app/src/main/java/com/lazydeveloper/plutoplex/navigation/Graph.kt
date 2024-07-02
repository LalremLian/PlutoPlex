package com.lazydeveloper.plutoplex.navigation

sealed class Graph (val route: String ){
    data object RootGraph: Graph("root_graph")
    data object HomeGraph: Graph("home_graph")
    data object NativeContentScreen: Screen("native_content_screen")
    data object WebGraph: Graph("web_graph")
    data object SearchGraph: Graph("search_graph")
}

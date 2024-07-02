package com.lazydeveloper.plutoplex.navigation

sealed class Screen(val route: String) {
    companion object {
        const val TITLE_ID_KEY = "title_id"
        const val COMIC_ID_KEY = "comic_id"
        const val SEASON_ID_KEY = "season_id"
        const val EPISODE_ID_KEY = "episode_id"
        const val SERVER_ID_KEY = "server_id"
    }
    data object HomeScreen: Screen("home_screen")
    data object PlayerScreen: Screen("player_screen/{$COMIC_ID_KEY}/{$SERVER_ID_KEY}"){
        fun passArguments(comicId: Int, serverId: Int): String {
            return "player_screen/$comicId/$serverId"
        }
    }
    data object SeriesPlayerScreen: Screen(
        "series_player_screen/{$COMIC_ID_KEY}/{$SEASON_ID_KEY}/{$EPISODE_ID_KEY}/{$SERVER_ID_KEY}"
    ){
        fun passArguments(comicId: Int, seasonId: Int, episodeId: Int, serverId: Int): String {
            return "series_player_screen/$comicId/$seasonId/$episodeId/$serverId"
        }
    }
    data object MovieScreen: Screen("movie_screen/{$COMIC_ID_KEY}"){
        fun passArguments(comicId: Int): String {
            return "movie_screen/$comicId"
        }
    }
    data object Media3Screen: Screen("media3_screen/{$COMIC_ID_KEY}/{$TITLE_ID_KEY}"){
        fun passArguments(url: String, movieTitle: String): String {
            return "media3_screen/$url/$movieTitle"
        }
    }
    data object SeriesScreen: Screen("series_screen/{$COMIC_ID_KEY}"){
        fun passArguments(comicId: Int): String {
            return "series_screen/$comicId"
        }
    }
    data object MoreScreen: Screen("more_screen")
    data object SplashScreen: Screen("splash_screen")
    data object SignInScreen: Screen("sign_in_screen")
    data object MovieDetailsScreen: Screen("details_screen/{$COMIC_ID_KEY}"){
        fun passArguments(comicId: Int): String {
            return "details_screen/$comicId"
        }
    }
    data object SeriesDetailsScreen: Screen("series_details_screen/{$COMIC_ID_KEY}"){
        fun passArguments(comicId: Int): String {
            return "series_details_screen/$comicId"
        }
    }
    data object SearchScreen: Screen("search_screen")
    data object ComicInfoScreen: Screen("comic_info_screen/{$COMIC_ID_KEY}") {
        fun passArguments(comicId: Int): String {
            return "comic_info_screen/$comicId"
        }
    }
    data object ComicReaderScreen: Screen("comic_reader_screen/{$COMIC_ID_KEY}") {
        fun passArguments(comicId: Int): String {
            return "comic_reader_screen/$comicId"
        }
    }
    data object PrivacyPolicyScreen: Screen("privacy_policy_screen")
    data object CopyrightScreen: Screen("copyright_screen")
    data object HistoryScreen: Screen("history_screen")
    data object FilterScreen: Screen("filter_screen/{$COMIC_ID_KEY}") {
        fun passArguments(comicId: Int): String {
            return "filter_screen/$comicId"
        }
    }

}

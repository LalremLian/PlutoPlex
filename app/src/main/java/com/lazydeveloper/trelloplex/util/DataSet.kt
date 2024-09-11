package com.lazydeveloper.trelloplex.util

import com.lazydeveloper.network.model.FilterItem
import com.lazydeveloper.network.model.global.Result

const val AffiliateLink = "https://affpa.top/L?tag=d_3343257m_97c_&site=3343257&ad=97"
const val VisitUs = "https://clvplex.blogspot.com/"
const val verrse_one = "Copyright Notice:\n" +
        "\n" +
        "All content, including but not limited to text, images, videos, and other media, on The Movie Database (https://www.themoviedb.org/) website is protected by copyright laws and international treaties. The content is the property of The Movie Database or its content suppliers and is provided for informational and personal use only.\n" +
        "\n" +
        "Lazy Developer, hereby referred to as \"Authorized User,\" is granted a limited license to access and use the content on The Movie Database website solely for its internal business purposes. This license allows the Authorized User to reproduce, distribute, modify, display, and perform the content, but solely for the purpose of enhancing its products and services.\n" +
        "\n" +
        "Any use of the content beyond the scope of this license, including but not limited to reproduction, distribution, modification, display, or performance for commercial purposes, requires the prior written consent of The Movie Database.\n" +
        "\n" +
        "Unauthorized use of the content may violate copyright laws and other applicable laws, leading to civil and criminal penalties.\n" +
        "\n" +
        "For inquiries regarding the use of content from The Movie Database beyond the scope of this license, please contact:\n" +
        "\n" +
        "[Contact Information]\n" +
        "\nhttps://www.themoviedb.org/about/get-in-touch/\n" +
        "\n" +
        "Thank you for respecting the copyright of The Movie Database."


object Types {
    val typeList = arrayListOf(
        FilterItem(0, "Movies"),
        FilterItem(1, "TV Series"),
    )
}

val movieList = listOf(
    Result(
        adult = false,
        backdropPath = "/6MKr3KgOLmzOP6MSuZERO41Lpkt.jpg",
        genreIds = listOf(28, 12, 14),
        id = 460465,
        mediaType = "movie",
        originalLanguage = "en",
        originalTitle = "Mortal Kombat",
        overview = "Washed-up MMA fighter Cole Young, unaware of his heritage, and hunted by Emperor Shang Tsung's best warrior, Sub-Zero, seeks out and trains with Earth's greatest champions as he prepares to stand against the enemies of Outworld in a high stakes battle for the universe.",
        popularity = 3173.0,
        posterPath = "/xGuOF1T3WmPsAcQEQJfnG7Ud9f8.jpg",
        releaseDate = "2021-04-07",
        title = "Mortal Kombat",
        video = false,
        voteAverage = 7.8,
        voteCount = 2452
    ),
    Result(
        adult = false,
        backdropPath = "/6MKr3KgOLmzOP6MSuZERO41Lpkt.jpg",
        genreIds = listOf(28, 12, 14),
        id = 460462,
        mediaType = "movie",
        originalLanguage = "en",
        originalTitle = "Mortal Kombat",
        overview = "Washed-up MMA fighter Cole Young, unaware of his heritage, and hunted by Emperor Shang Tsung's best warrior, Sub-Zero, seeks out and trains with Earth's greatest champions as he prepares to stand against the enemies of Outworld in a high stakes battle for the universe.",
        popularity = 3173.0,
        posterPath = "/xGuOF1T3WmPsAcQEQJfnG7Ud9f8.jpg",
        releaseDate = "2021-04-07",
        title = "Mortal Kombat 2",
        video = false,
        voteAverage = 7.8,
        voteCount = 2452
    ),
)
val genres = listOf(
    FilterItem(28, "Action"),
    FilterItem(12, "Adventure"),
    FilterItem(16, "Animation"),
    FilterItem(35, "Comedy"),
    FilterItem(80, "Crime"),
    FilterItem(99, "Documentary"),
    FilterItem(18, "Drama"),
    FilterItem(10751, "Family"),
    FilterItem(14, "Fantasy"),
    FilterItem(36, "History"),
    FilterItem(27, "Horror"),
    FilterItem(10402, "Music"),
    FilterItem(9648, "Mystery"),
    FilterItem(10749, "Romance"),
    FilterItem(878, "Science Fiction"),
    FilterItem(10770, "TV Movie"),
    FilterItem(53, "Thriller"),
    FilterItem(10752, "War"),
    FilterItem(37, "Western")
)

val countries = listOf(
    FilterItem(1, "United States", "US"),
    FilterItem(2, "Canada", "CA"),
    FilterItem(3, "United Kingdom", "GB"),
    FilterItem(4, "Australia", "AU"),
    FilterItem(5, "Germany", "DE"),
    FilterItem(6, "France", "FR"),
    FilterItem(7, "Italy", "IT"),
    FilterItem(8, "Spain", "ES"),
    FilterItem(9, "Russia", "RU"),
    FilterItem(10, "China", "CN"),
    FilterItem(11, "Japan", "JP"),
    FilterItem(12, "India", "IN"),
)
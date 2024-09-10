package com.lazydeveloper.network.model

data class FireStoreServer(
    val server: Boolean = false,
    val player: Boolean = false,
    val version: String = "",
    val description: String = "",
    val updatemsg: String = "",
    val showAdmobBanner: Boolean = false,
    val tmdbImagePath: String = "",
    val tmdbImagePathBackCover: String = "",
    val serverOne: String = "",
    val serverTwo: String = "",
    val serverTwoTv: String = "",
    val telegramLink: String = "",
    val appUrl: String = "",
    val showExitAds: Boolean = false,
    val blogSite: String = "",
    val visitUs: String = "",
)
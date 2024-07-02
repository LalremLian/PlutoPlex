package com.lazydeveloper.network.api

import com.lazydeveloper.network.model.FttpMoviePlalyerResponse
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface ApiInterfaceFTTP {
    //----------------------------------Movie Player with Details
    @GET("/api/v1/byid.php")
    suspend fun getMovieWatchFile(
        @Query("id") movieId: String = "",
        @HeaderMap headers: Map<String, String> = mapOf(
            "accept" to "application/json",
        ),
    ): FttpMoviePlalyerResponse

    //----------------------------------Tv Player with Details
//    @GET("/api/v1/tvepisodes.php")
//    suspend fun getTvWatchFile(
//        @Query("tvid") movieId: String = "",
//        @HeaderMap headers: Map<String, String> = mapOf(
//            "accept" to "application/json",
//        ),
//    ): FttpTvPlayerResponse
}
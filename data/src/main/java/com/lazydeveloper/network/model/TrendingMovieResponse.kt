package com.lazydeveloper.network.model


import com.google.gson.annotations.SerializedName
import com.lazydeveloper.network.model.global.Result

data class TrendingMovieResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<Result>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)
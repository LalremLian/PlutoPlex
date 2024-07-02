package com.lazydeveloper.network.model


import com.google.gson.annotations.SerializedName

class FttpMoviePlalyerResponse : ArrayList<FttpMoviePlalyerResponse.FttpMoviePlalyerResponseItem>(){
    data class FttpMoviePlalyerResponseItem(
        @SerializedName("backdrops_Poster")
        val backdropsPoster: Any?,
        @SerializedName("DownHit")
        val downHit: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("MovieActors")
        val movieActors: String?,
        @SerializedName("MovieCategory")
        val movieCategory: String?,
        @SerializedName("MovieDate")
        val movieDate: String?,
        @SerializedName("MovieGenre")
        val movieGenre: String?,
        @SerializedName("MovieID")
        val movieID: String?,
        @SerializedName("MovieKeywords")
        val movieKeywords: Any?,
        @SerializedName("MovieQuality")
        val movieQuality: String?,
        @SerializedName("MovieRatings")
        val movieRatings: String?,
        @SerializedName("MovieRuntime")
        val movieRuntime: String?,
        @SerializedName("MovieSize")
        val movieSize: String?,
        @SerializedName("MovieStory")
        val movieStory: String?,
        @SerializedName("MovieSubtitle")
        val movieSubtitle: Any?,
        @SerializedName("MovieTitle")
        val movieTitle: String?,
        @SerializedName("MovieTrailer")
        val movieTrailer: String?,
        @SerializedName("MovieWatchLink")
        val movieWatchLink: String?,
        @SerializedName("MovieYear")
        val movieYear: String?,
        @SerializedName("Moviehomepage")
        val moviehomepage: String?,
        @SerializedName("Movielang")
        val movielang: String?,
        @SerializedName("poster")
        val poster: String?,
        @SerializedName("published")
        val published: String?,
        @SerializedName("uploadTime")
        val uploadTime: String?,
        @SerializedName("uploadedUser")
        val uploadedUser: String?,
        @SerializedName("views")
        val views: Any?
    )
}
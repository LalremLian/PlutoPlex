package com.lazydeveloper.network.model


import com.google.gson.annotations.SerializedName

data class ExternalResponse(
    @SerializedName("facebook_id")
    val facebookId: Any?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("instagram_id")
    val instagramId: Any?,
    @SerializedName("twitter_id")
    val twitterId: Any?,
    @SerializedName("wikidata_id")
    val wikidataId: Any?
)
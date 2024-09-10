package com.lazydeveloper.network.model


import com.google.gson.annotations.SerializedName

data class FilteredMoveResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("timestamp")
    val timestamp: Long?
) {
    data class Data(
        @SerializedName("list")
        val list: List<asd>?,
        @SerializedName("pageInfo")
        val pageInfo: PageInfo?
    ) {
        data class asd(
            @SerializedName("title")
            val title: Title?
        ) {
            data class Title(
                @SerializedName("canRateTitle")
                val canRateTitle: CanRateTitle?,
                @SerializedName("id")
                val id: String?,
                @SerializedName("isAdult")
                val isAdult: Boolean?,
                @SerializedName("originalTitleText")
                val originalTitleText: OriginalTitleText?,
                @SerializedName("primaryImage")
                val primaryImage: PrimaryImage?,
                @SerializedName("ratingsSummary")
                val ratingsSummary: RatingsSummary?,
                @SerializedName("releaseYear")
                val releaseYear: ReleaseYear?,
                @SerializedName("series")
                val series: Any?,
                @SerializedName("titleEpisode")
                val titleEpisode: Any?,
                @SerializedName("titleText")
                val titleText: TitleText?,
                @SerializedName("titleType")
                val titleType: TitleType?
            ) {
                data class CanRateTitle(
                    @SerializedName("isRatable")
                    val isRatable: Boolean?
                )

                data class OriginalTitleText(
                    @SerializedName("text")
                    val text: String?
                )

                data class PrimaryImage(
                    @SerializedName("id")
                    val id: String?,
                    @SerializedName("imageHeight")
                    val imageHeight: Int?,
                    @SerializedName("imageUrl")
                    val imageUrl: String?,
                    @SerializedName("imageWidth")
                    val imageWidth: Int?
                )

                data class RatingsSummary(
                    @SerializedName("aggregateRating")
                    val aggregateRating: Double?,
                    @SerializedName("topRanking")
                    val topRanking: TopRanking?,
                    @SerializedName("voteCount")
                    val voteCount: Int?
                ) {
                    data class TopRanking(
                        @SerializedName("rank")
                        val rank: Int?
                    )
                }

                data class ReleaseYear(
                    @SerializedName("endYear")
                    val endYear: Any?,
                    @SerializedName("year")
                    val year: Int?
                )

                data class TitleText(
                    @SerializedName("text")
                    val text: String?
                )

                data class TitleType(
                    @SerializedName("canHaveEpisodes")
                    val canHaveEpisodes: Boolean?,
                    @SerializedName("categories")
                    val categories: List<Category?>?,
                    @SerializedName("displayableProperty")
                    val displayableProperty: DisplayableProperty?,
                    @SerializedName("id")
                    val id: String?,
                    @SerializedName("isEpisode")
                    val isEpisode: Boolean?,
                    @SerializedName("isSeries")
                    val isSeries: Boolean?,
                    @SerializedName("text")
                    val text: String?
                ) {
                    data class Category(
                        @SerializedName("id")
                        val id: String?,
                        @SerializedName("text")
                        val text: String?,
                        @SerializedName("value")
                        val value: String?
                    )

                    data class DisplayableProperty(
                        @SerializedName("value")
                        val value: Value?
                    ) {
                        data class Value(
                            @SerializedName("plainText")
                            val plainText: String?
                        )
                    }
                }
            }
        }

        data class PageInfo(
            @SerializedName("endCursor")
            val endCursor: String?,
            @SerializedName("hasNextPage")
            val hasNextPage: Boolean?,
            @SerializedName("total")
            val total: Int?
        )
    }
}
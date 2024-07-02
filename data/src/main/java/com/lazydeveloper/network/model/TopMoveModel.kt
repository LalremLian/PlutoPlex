package com.lazydeveloper.network.model

import com.google.gson.annotations.SerializedName

data class TopMoveModel(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("timestamp")
    val timestamp: Long?
) {
    data class Data(
        @SerializedName("canRateTitle")
        val canRateTitle: CanRateTitle?,
        @SerializedName("chartMeterRanking")
        val chartMeterRanking: ChartMeterRanking?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("isAdult")
        val isAdult: Boolean?,
        @SerializedName("originalTitleText")
        val originalTitleText: OriginalTitleText?,
        @SerializedName("plot")
        val plot: Plot?,
        @SerializedName("primaryImage")
        val primaryImage: PrimaryImage?,
        @SerializedName("ratingsSummary")
        val ratingsSummary: RatingsSummary?,
        @SerializedName("releaseDate")
        val releaseDate: ReleaseDate?,
        @SerializedName("releaseYear")
        val releaseYear: ReleaseYear?,
        @SerializedName("series")
        val series: Any?,
        @SerializedName("titleCertificate")
        val titleCertificate: TitleCertificate?,
        @SerializedName("titleEpisode")
        val titleEpisode: Any?,
        @SerializedName("titleRuntime")
        val titleRuntime: TitleRuntime?,
        @SerializedName("titleText")
        val titleText: TitleText?,
        @SerializedName("titleType")
        val titleType: TitleType?,
        @SerializedName("watchOptionsByCategory")
        val watchOptionsByCategory: WatchOptionsByCategory?
    ) {
        data class CanRateTitle(
            @SerializedName("isRatable")
            val isRatable: Boolean?
        )

        data class ChartMeterRanking(
            @SerializedName("currentRank")
            val currentRank: Int?,
            @SerializedName("rankChange")
            val rankChange: RankChange?
        ) {
            data class RankChange(
                @SerializedName("changeDirection")
                val changeDirection: String?,
                @SerializedName("difference")
                val difference: Int?
            )
        }

        data class OriginalTitleText(
            @SerializedName("text")
            val text: String?
        )

        data class Plot(
            @SerializedName("author")
            val author: Any?,
            @SerializedName("correctionLink")
            val correctionLink: CorrectionLink?,
            @SerializedName("id")
            val id: String?,
            @SerializedName("plotText")
            val plotText: PlotText?,
            @SerializedName("reportingLink")
            val reportingLink: ReportingLink?
        ) {
            data class CorrectionLink(
                @SerializedName("url")
                val url: String?
            )

            data class PlotText(
                @SerializedName("plainText")
                val plainText: String?
            )

            data class ReportingLink(
                @SerializedName("url")
                val url: String?
            )
        }

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

        data class ReleaseDate(
            @SerializedName("country")
            val country: Country?,
            @SerializedName("day")
            val day: Int?,
            @SerializedName("month")
            val month: Int?,
            @SerializedName("releaseAttributes")
            val releaseAttributes: List<ReleaseAttribute?>?,
            @SerializedName("restriction")
            val restriction: Any?,
            @SerializedName("year")
            val year: Int?
        ) {
            data class Country(
                @SerializedName("id")
                val id: String?,
                @SerializedName("text")
                val text: String?
            )

            data class ReleaseAttribute(
                @SerializedName("text")
                val text: String?
            )
        }

        data class ReleaseYear(
            @SerializedName("endYear")
            val endYear: Int?,
            @SerializedName("year")
            val year: Int?
        )

        data class TitleCertificate(
            @SerializedName("certificateCountry")
            val certificateCountry: CertificateCountry?,
            @SerializedName("rating")
            val rating: String?,
            @SerializedName("ratingReason")
            val ratingReason: String?
        ) {
            data class CertificateCountry(
                @SerializedName("id")
                val id: String?,
                @SerializedName("text")
                val text: String?
            )
        }

        data class TitleRuntime(
            @SerializedName("displayableProperty")
            val displayableProperty: DisplayableProperty?,
            @SerializedName("seconds")
            val seconds: Int?
        ) {
            data class DisplayableProperty(
                @SerializedName("qualifiersInMarkdownList")
                val qualifiersInMarkdownList: List<QualifiersInMarkdown?>?
            ) {
                data class QualifiersInMarkdown(
                    @SerializedName("plainText")
                    val plainText: String?
                )
            }
        }

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

        data class WatchOptionsByCategory(
            @SerializedName("categorizedWatchOptionsList")
            val categorizedWatchOptionsList: List<CategorizedWatchOptions?>?
        ) {
            data class CategorizedWatchOptions(
                @SerializedName("watchOptions")
                val watchOptions: List<WatchOption?>?
            ) {
                data class WatchOption(
                    @SerializedName("description")
                    val description: Description?,
                    @SerializedName("link")
                    val link: String?,
                    @SerializedName("provider")
                    val provider: Provider?,
                    @SerializedName("shortDescription")
                    val shortDescription: ShortDescription?,
                    @SerializedName("title")
                    val title: Title?
                ) {
                    data class Description(
                        @SerializedName("value")
                        val value: String?
                    )

                    data class Provider(
                        @SerializedName("categoryType")
                        val categoryType: String?,
                        @SerializedName("description")
                        val description: Description?,
                        @SerializedName("id")
                        val id: String?,
                        @SerializedName("logos")
                        val logos: Logos?,
                        @SerializedName("name")
                        val name: Name?,
                        @SerializedName("refTagFragment")
                        val refTagFragment: String?
                    ) {
                        data class Description(
                            @SerializedName("value")
                            val value: String?
                        )

                        data class Logos(
                            @SerializedName("icon")
                            val icon: Icon?,
                            @SerializedName("slate")
                            val slate: Slate?
                        ) {
                            data class Icon(
                                @SerializedName("height")
                                val height: Int?,
                                @SerializedName("url")
                                val url: String?,
                                @SerializedName("width")
                                val width: Int?
                            )

                            data class Slate(
                                @SerializedName("height")
                                val height: Int?,
                                @SerializedName("url")
                                val url: String?,
                                @SerializedName("width")
                                val width: Int?
                            )
                        }

                        data class Name(
                            @SerializedName("value")
                            val value: String?
                        )
                    }

                    data class ShortDescription(
                        @SerializedName("value")
                        val value: String?
                    )

                    data class Title(
                        @SerializedName("value")
                        val value: String?
                    )
                }
            }
        }
    }
}
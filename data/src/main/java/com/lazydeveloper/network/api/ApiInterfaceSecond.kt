package com.lazydeveloper.network.api

import com.lazydeveloper.network.model.DetailsResponse
import com.lazydeveloper.network.model.ExternalResponse
import com.lazydeveloper.network.model.GenreResponse
import com.lazydeveloper.network.model.MovieFilterResponse
import com.lazydeveloper.network.model.PopularMovieResponse
import com.lazydeveloper.network.model.PopularTvResponse
import com.lazydeveloper.network.model.SearchResponse
import com.lazydeveloper.network.model.SeriesDetailsResponse
import com.lazydeveloper.network.model.TopRatedMovieResponse
import com.lazydeveloper.network.model.TrendingAllResponse
import com.lazydeveloper.network.model.TrendingMovieResponse
import com.lazydeveloper.network.model.TrendingSeriesResponse
import com.lazydeveloper.network.model.TvFilterResponse
import com.lazydeveloper.util.API_TOKEN
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterfaceSecond {
    //----------------------------------Genre List
    @GET("/3/genre/movie/list?language=en")
    suspend fun genreList(
        @Query("page") nextPage: Int,
        @HeaderMap headers: Map<String, String> = mapOf(
            "Authorization" to "Bearer $API_TOKEN",
            "accept" to "application/json",
        ),
    ): GenreResponse

    //----------------------------------Filter TV
    @GET("/3/discover/tv?language=en-US")
    suspend fun filterTv(
        @Query("page") nextPage: Int,
        @Query("first_air_date_year") firstAirDateYear: String = "",
        @Query("include_adult") includeAdult: Boolean = true,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_genres") withGenres: String = "",
        @Query("with_origin_country") withOriginCountry: String = "",
        @HeaderMap headers: Map<String, String> = mapOf(
            "Authorization" to "Bearer $API_TOKEN",
            "accept" to "application/json",
        ),
    ): TvFilterResponse

    //----------------------------------Filter Movie
    @GET("/3/discover/movie?language=en-US")
    suspend fun filterMovie(
        @Query("page") nextPage: Int,
        @Query("first_air_date_year") firstAirDateYear: String = "",
        @Query("include_adult") includeAdult: Boolean = true,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_genres") withGenres: String = "",
        @Query("with_origin_country") withOriginCountry: String = "",
        @HeaderMap headers: Map<String, String> = mapOf(
            "Authorization" to "Bearer $API_TOKEN",
            "accept" to "application/json",
        ),
    ): MovieFilterResponse


    //----------------------------------Search
    @GET("/3/search/multi?include_adult=false&language=en-US&page=1")
    suspend fun multiSearch(
        @Query("query") query: String,
        @HeaderMap headers: Map<String, String> = mapOf(
            "Authorization" to "Bearer $API_TOKEN",
            "accept" to "application/json",
        ),
    ): SearchResponse

    @GET("/3/movie/top_rated?language=en-US&page=1")
    suspend fun getTopRatedMoviesT(
        @HeaderMap headers: Map<String, String> = mapOf(
            "Authorization" to "Bearer $API_TOKEN",
            "accept" to "application/json",
        ),
    ): TopRatedMovieResponse

    //----------------------------------Trending All
    @GET("/3/trending/all/week?language=en-US")
    suspend fun getTrendingAll(
        @Query("page") nextPage: Int,
        @HeaderMap headers: Map<String, String> = mapOf(
            "Authorization" to "Bearer $API_TOKEN",
            "accept" to "application/json",
        ),
    ): TrendingAllResponse


    //----------------------------------Popular Movies
    @GET("/3/movie/popular?language=en-US")
    suspend fun getPopularMoviesTmdb(
        @Query("page") nextPage: Int,
        @HeaderMap headers: Map<String, String> = mapOf(
            "Authorization" to "Bearer $API_TOKEN",
            "accept" to "application/json",
        ),
    ): PopularMovieResponse

    //----------------------------------Trending Movies
    @GET("/3/trending/movie/week?language=en-US")
    suspend fun getTrendingMovies(
        @Query("page") nextPage: Int,
        @HeaderMap headers: Map<String, String> = mapOf(
            "Authorization" to "Bearer $API_TOKEN",
            "accept" to "application/json",
        ),
    ): TrendingMovieResponse

    //----------------------------------Trending TV shows
    @GET("/3/trending/tv/week?language=en-US")
    suspend fun getTrendingSeries(
        @Query("page") nextPage: Int,
        @HeaderMap headers: Map<String, String> = mapOf(
            "Authorization" to "Bearer $API_TOKEN",
            "accept" to "application/json",
        ),
    ): TrendingSeriesResponse


    //----------------------------------Popular TV
    @GET("/3/tv/popular?language=en-US")
    suspend fun getPopularTvTmdb(
        @Query("page") nextPage: Int,
        @HeaderMap headers: Map<String, String> = mapOf(
            "Authorization" to "Bearer $API_TOKEN",
            "accept" to "application/json",
        ),
    ): PopularTvResponse


    //----------------------------------Movie Details
    @GET("/3/movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = "en-US",
        @HeaderMap headers: Map<String, String> = mapOf(
            "Authorization" to "Bearer $API_TOKEN",
            "accept" to "application/json",
        ),
    ): DetailsResponse

    //----------------------------------TV Details
    @GET("/3/tv/{movieId}")
    suspend fun getSeriesDetails(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = "en-US",
        @HeaderMap headers: Map<String, String> = mapOf(
            "Authorization" to "Bearer $API_TOKEN",
            "accept" to "application/json",
        ),
    ): SeriesDetailsResponse

    @GET("/3/movie/{movieId}/external_ids")
    suspend fun getExternalMovieId(
        @Path("movieId") movieId: Int,
        @HeaderMap headers: Map<String, String> = mapOf(
            "Authorization" to "Bearer $API_TOKEN",
            "accept" to "application/json",
        ),
    ): ExternalResponse

}
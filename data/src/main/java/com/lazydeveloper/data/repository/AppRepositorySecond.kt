package com.lazydeveloper.data.repository

import com.lazydeveloper.network.api.ApiInterfaceSecond
import javax.inject.Inject

class AppRepositorySecond @Inject constructor(
    private val api: ApiInterfaceSecond,
//    private val apiFttp: ApiInterfaceFTTP,
) {
    //    suspend fun fetchMovies()= api.getPopularMovies()
    suspend fun filterTv(
        nextPage: Int,
        year: String,
        genre: String,
        country: String,
    ) = api.filterTv(
        nextPage = nextPage,
        firstAirDateYear = year,
        withGenres = genre,
        withOriginCountry = country
    )

    suspend fun filterMovie(
        nextPage: Int,
        year: String,
        genre: String,
        country: String,
    ) = api.filterMovie(
        nextPage = nextPage,
        firstAirDateYear = year,
        withGenres = genre,
        withOriginCountry = country
    )

    suspend fun multiSearch(query: String) = api.multiSearch(query)
    suspend fun fetchTopRatedMoviesPOST(nextPage: Int) = api.getPopularMoviesTmdb(nextPage)
    suspend fun fetchTrendingMovies(nextPage: Int) = api.getTrendingMovies(nextPage)
    suspend fun fetchTrendingSeries(nextPage: Int) = api.getTrendingSeries(nextPage)
    suspend fun fetchTopRatedTv(nextPage: Int) = api.getPopularTvTmdb(nextPage)
    suspend fun fetchTopRatedMovie() = api.getTopRatedMoviesT()
    suspend fun fetchTrendingAll() = api.getTrendingAll(1)
    suspend fun fetchMovieDetails(movieId: Int) = api.getMovieDetails(movieId)
    suspend fun fetchSeriesDetails(seriesId: Int) = api.getSeriesDetails(seriesId)
    suspend fun fetchExternalMovie(movieId: Int) = api.getExternalMovieId(movieId)


    //FTTP
//    suspend fun getMovieWatchFile(movieId: String) = apiFttp.getMovieWatchFile(movieId)

}
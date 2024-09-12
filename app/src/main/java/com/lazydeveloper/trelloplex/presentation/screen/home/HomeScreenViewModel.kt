package com.lazydeveloper.trelloplex.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazydeveloper.data.Resource
import com.lazydeveloper.data.preference.SessionPreference
import com.lazydeveloper.data.repository.AppRepositorySecond
import com.lazydeveloper.network.model.PopularMovieResponse
import com.lazydeveloper.network.model.PopularTvResponse
import com.lazydeveloper.network.model.TopRatedMovieResponse
import com.lazydeveloper.network.model.TrendingAllResponse
import com.lazydeveloper.network.model.TrendingMovieResponse
import com.lazydeveloper.network.model.TrendingSeriesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
//    private val appRepo: AppRepository
    private val appRepo: AppRepositorySecond,
    sessionPreference: SessionPreference
) : ViewModel() {

    val mPref = sessionPreference

    private val _stateFlow = MutableStateFlow<Resource<PopularMovieResponse>>(Resource.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    private val _trendingAllFlow = MutableStateFlow<Resource<TrendingAllResponse>>(Resource.Loading)
    val trendingAllFlow = _trendingAllFlow.asStateFlow()

    private val _trendingMovieFlow = MutableStateFlow<Resource<TrendingMovieResponse>>(Resource.Loading)
    val trendingMovieFlow = _trendingMovieFlow.asStateFlow()

    private val _trendingSeriesFlow = MutableStateFlow<Resource<TrendingSeriesResponse>>(Resource.Loading)
    val trendingSeriesFlow = _trendingSeriesFlow.asStateFlow()

    private val _popularTvFlow = MutableStateFlow<Resource<PopularTvResponse>>(Resource.Loading)
    val popularTvFlow = _popularTvFlow.asStateFlow()

    private val _topRatedMovieFlow = MutableStateFlow<Resource<TopRatedMovieResponse>>(Resource.Loading)
    val topRatedMovieFlow = _topRatedMovieFlow.asStateFlow()

    init {
        fetchTrendingAll()
//        fetchTopRatedMovie()
        fetchTrendingMovies()
        fetchTrendingSeries()
        fetchPolularMovies()
        fetchTvSeries()
    }

    //header
    private fun fetchTopRatedMovie() {
        viewModelScope.launch {
            try {
                val response = appRepo.fetchTopRatedMovie()
                _topRatedMovieFlow.value = Resource.Success(response)
            } catch (e: Exception) {
                _topRatedMovieFlow.value = Resource.Error(e.message ?: "Failed to fetch data")
            }
        }
    }
    private fun fetchTrendingAll() {
        viewModelScope.launch {
            try {
                val response = appRepo.fetchTrendingAll()
                _trendingAllFlow.value = Resource.Success(response)
            } catch (e: Exception) {
                _trendingAllFlow.value = Resource.Error(e.message ?: "Failed to fetch data")
            }
        }
    }
    private fun fetchTrendingMovies() {
        viewModelScope.launch {
            try {
//                val response = appRepo.fetchMovies()
                val response = appRepo.fetchTrendingMovies(1)
                _trendingMovieFlow.value = Resource.Success(response)
            } catch (e: Exception) {
                _trendingMovieFlow.value = Resource.Error(e.message ?: "Failed to fetch data")
            }
        }
    }
    private fun fetchTrendingSeries() {
        viewModelScope.launch {
            try {
                val response = appRepo.fetchTrendingSeries(1)
                _trendingSeriesFlow.value = Resource.Success(response)
            } catch (e: Exception) {
                _trendingSeriesFlow.value = Resource.Error(e.message ?: "Failed to fetch data")
            }
        }
    }
    private fun fetchPolularMovies() {
        viewModelScope.launch {
            try {
                val response = appRepo.fetchTopRatedMoviesPOST(1)
                _stateFlow.value = Resource.Success(response)
            } catch (e: Exception) {
                _stateFlow.value = Resource.Error(e.message ?: "Failed to fetch data")
            }
        }
    }
    private fun fetchTvSeries() {
        viewModelScope.launch {
            try {
                val response = appRepo.fetchTopRatedTv(1)
                _popularTvFlow.value = Resource.Success(response)
            } catch (e: Exception) {
                _popularTvFlow.value = Resource.Error(e.message ?: "Failed to fetch data")
            }
        }
    }
}
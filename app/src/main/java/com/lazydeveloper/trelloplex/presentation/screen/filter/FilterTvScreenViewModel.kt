package com.lazydeveloper.trelloplex.presentation.screen.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lazydeveloper.data.paging.FilterMoviePagingSource
import com.lazydeveloper.data.paging.FilterTvPagingSource
import com.lazydeveloper.data.preference.SessionPreference
import com.lazydeveloper.data.repository.AppRepositorySecond
import com.lazydeveloper.network.model.MovieFilterResponse
import com.lazydeveloper.network.model.TvFilterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@HiltViewModel
class FilterTvScreenViewModel @Inject constructor(
    private val appRepo: AppRepositorySecond,
    sessionPreference: SessionPreference
): ViewModel() {
    val mPref = sessionPreference

    private val _popularMoviesFlow = MutableStateFlow<Flow<PagingData<TvFilterResponse.Result>>>(emptyFlow())
    val popularMoviesFlow: StateFlow<Flow<PagingData<TvFilterResponse.Result>>> = _popularMoviesFlow.asStateFlow()

    private val _movieFilterFlow = MutableStateFlow<Flow<PagingData<MovieFilterResponse.Result>>>(emptyFlow())
    val movieFilterFlow: StateFlow<Flow<PagingData<MovieFilterResponse.Result>>> = _movieFilterFlow.asStateFlow()

    fun getFilterTvFlow(
        year: String,
        genre: List<String>,
        country: String
    ){
        _popularMoviesFlow.value = Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { FilterTvPagingSource(appRepo, year, genre, country) }
        ).flow.cachedIn(viewModelScope)
    }

    fun getFilterMovieFlow(
        year: String,
        genre: List<String>,
        country: String
    ){
        _movieFilterFlow.value = Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { FilterMoviePagingSource(appRepo, year, genre, country) }
        ).flow.cachedIn(viewModelScope)
    }
}
package com.lazydeveloper.trelloplex.presentation.screen.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.lazydeveloper.data.paging.MoviePagingSource
import com.lazydeveloper.data.paging.TrendingMoviePagingSource
import com.lazydeveloper.data.preference.SessionPreference
import com.lazydeveloper.data.repository.AppRepositorySecond
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieScreenViewModel @Inject constructor(
    private val appRepo: AppRepositorySecond,
    sessionPreference: SessionPreference
): ViewModel() {
    val mPref = sessionPreference

    val popularMoviesFlow = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { MoviePagingSource(appRepo) }
    ).flow.cachedIn(viewModelScope)

    val trendingMoviesFlow = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { TrendingMoviePagingSource(appRepo) }
    ).flow.cachedIn(viewModelScope)
}

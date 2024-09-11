package com.lazydeveloper.trelloplex.presentation.screen.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.lazydeveloper.data.paging.SeriesPagingSource
import com.lazydeveloper.data.paging.TrendingSeriesPagingSource
import com.lazydeveloper.data.repository.AppRepositorySecond
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeriesScreenViewModel @Inject constructor(
    private val appRepo: AppRepositorySecond
): ViewModel(){

    val popularSeriesFlow = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { SeriesPagingSource(appRepo) }
    ).flow.cachedIn(viewModelScope)

    val trendingSeriesFlow = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { TrendingSeriesPagingSource(appRepo) }
    ).flow.cachedIn(viewModelScope)
}
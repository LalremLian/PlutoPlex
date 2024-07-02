package com.lazydeveloper.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lazydeveloper.data.repository.AppRepositorySecond
import com.lazydeveloper.network.model.TrendingMovieResponse

class TrendingMoviePagingSource(
    private val appRepo: AppRepositorySecond
) : PagingSource<Int, TrendingMovieResponse.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TrendingMovieResponse.Result> {
        return try {
            val nextPage = params.key ?: 1
            val response = appRepo.fetchTrendingMovies(nextPage)
            LoadResult.Page(
                data = response.results?: emptyList(),
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            Log.e("TAG", "error2: ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TrendingMovieResponse.Result>): Int? {
        return state.anchorPosition
    }

}
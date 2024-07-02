package com.lazydeveloper.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lazydeveloper.data.repository.AppRepositorySecond
import com.lazydeveloper.network.model.PopularTvResponse

class SeriesPagingSource(
    private val appRepo: AppRepositorySecond
) : PagingSource<Int, PopularTvResponse.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularTvResponse.Result> {
        return try {
            val nextPage = params.key ?: 1
            val response = appRepo.fetchTopRatedTv(nextPage)
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

    override fun getRefreshKey(state: PagingState<Int, PopularTvResponse.Result>): Int? {
        return state.anchorPosition
    }

}
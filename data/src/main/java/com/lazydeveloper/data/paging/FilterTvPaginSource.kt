package com.lazydeveloper.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lazydeveloper.data.repository.AppRepositorySecond
import com.lazydeveloper.network.model.TvFilterResponse

class FilterTvPagingSource(
    private val appRepo: AppRepositorySecond,
    private val year: String,
    private val genre: List<String> = emptyList(),
    private val country: String
) : PagingSource<Int, TvFilterResponse.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvFilterResponse.Result> {
        return try {
            val nextPage = params.key ?: 1
            val response = appRepo.filterTv(
                nextPage = nextPage,
                year = year.toString(),
                genre = genre.joinToString(separator = "&"),
                country = country
            )
            val nextKey = if (response.results.isNullOrEmpty()) null else nextPage + 1
            LoadResult.Page(
                data = response.results?: emptyList(),
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Log.e("TAG", "error2: ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TvFilterResponse.Result>): Int? {
        return state.anchorPosition
    }

}
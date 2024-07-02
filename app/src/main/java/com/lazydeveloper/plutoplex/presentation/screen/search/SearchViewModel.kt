package com.lazydeveloper.plutoplex.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazydeveloper.data.Resource
import com.lazydeveloper.data.repository.AppRepositorySecond
import com.lazydeveloper.network.model.SearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val appRepo: AppRepositorySecond
): ViewModel(){

    private val _searchFlow = MutableStateFlow<Resource<SearchResponse>>(Resource.Loading)
    val searchFlow = _searchFlow.asStateFlow()

    //header
    fun fetchTopRatedMovie(query: String) {
        viewModelScope.launch {
            try {
                val response = appRepo.multiSearch(query)
                _searchFlow.value = Resource.Success(response)
            } catch (e: Exception) {
                _searchFlow.value = Resource.Error(e.message ?: "Failed to fetch data")
            }
        }
    }
}
package com.lazydeveloper.plutoplex.presentation.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazydeveloper.data.Resource
import com.lazydeveloper.data.repository.AppRepositorySecond
import com.lazydeveloper.data.repository.DatabaseRepo
import com.lazydeveloper.database.entities.HistoryEntity
import com.lazydeveloper.network.model.SeriesDetailsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesDetailsScreenViewModel @Inject constructor(
    private val appRepo: AppRepositorySecond,
    private val dbRepo: DatabaseRepo
): ViewModel()  {
    private val _seriesDetailsFlow = MutableStateFlow<Resource<SeriesDetailsResponse>>(Resource.Loading)
    val seriesDetailsFlow = _seriesDetailsFlow.asStateFlow()
    val searchHistoryListFlow2 = MutableStateFlow<List<HistoryEntity>>(emptyList())
    fun fetchPosts(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = appRepo.fetchSeriesDetails(movieId)
                _seriesDetailsFlow.value = Resource.Success(response)
            } catch (e: Exception) {
                _seriesDetailsFlow.value = Resource.Error(e.message ?: "Failed to fetch data")
            }
        }
    }
    fun addHistory(searchHistoryEntity: HistoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val isDuplicate = dbRepo.checkDuplicateHistory(searchHistoryEntity.movieId)
            if (isDuplicate.isEmpty()) {
                val count = dbRepo.getHistoryRowCount()
                if (count == 30) {
                    dbRepo.deleteLastHistory()
                }
                dbRepo.addHistory(searchHistoryEntity)
            }
        }
    }
}
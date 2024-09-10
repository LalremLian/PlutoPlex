package com.lazydeveloper.plutoplex.presentation.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazydeveloper.data.Resource
import com.lazydeveloper.data.repository.AppRepositorySecond
import com.lazydeveloper.data.repository.DatabaseRepo
import com.lazydeveloper.database.entities.HistoryEntity
import com.lazydeveloper.network.model.DetailsResponse
import com.lazydeveloper.network.model.ExternalResponse
import com.lazydeveloper.network.model.FttpMoviePlalyerResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsScreenViewModel @Inject constructor(
    private val appRepo: AppRepositorySecond,
    private val dbRepo: DatabaseRepo
): ViewModel(){
    private val _movieDetailsFlow = MutableStateFlow<Resource<DetailsResponse>>(Resource.Loading)
    val movieDetailsFlow = _movieDetailsFlow.asStateFlow()

    private val _externalStateFlow = MutableStateFlow<Resource<ExternalResponse>>(Resource.Loading)
    val externalStateFlow = _externalStateFlow.asStateFlow()

    private val _fttpMovieStateFlow =
        MutableStateFlow<Resource<FttpMoviePlalyerResponse>>(Resource.Loading)
    val fttpMovieStateFlow = _fttpMovieStateFlow.asStateFlow()

    fun fetchExternalMovieIds(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = appRepo.fetchExternalMovie(movieId)
                _externalStateFlow.value = Resource.Success(response)
            } catch (e: Exception) {
                _externalStateFlow.value = Resource.Error(e.message ?: "Failed to fetch data")
            }
        }
    }

//    fun fetchMoveWatchFile(movieId: String) {
//        Log.e("MovieDetailsScreenViewModel", "fetchMoveWatchFile: $movieId")
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = appRepo.getMovieWatchFile(movieId)
//                _fttpMovieStateFlow.value = Resource.Success(response)
//            } catch (e: Exception) {
//                _fttpMovieStateFlow.value = Resource.Error(e.message ?: "Failed to fetch data")
//            }
//        }
//    }

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = appRepo.fetchMovieDetails(movieId)
                _movieDetailsFlow.value = Resource.Success(response)
            } catch (e: Exception) {
                _movieDetailsFlow.value = Resource.Error(e.message ?: "Failed to fetch data")
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
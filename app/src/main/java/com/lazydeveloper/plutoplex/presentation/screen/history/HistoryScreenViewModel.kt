package com.lazydeveloper.plutoplex.presentation.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazydeveloper.data.repository.DatabaseRepo
import com.lazydeveloper.database.entities.HistoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val dbRepo: DatabaseRepo
) : ViewModel() {
    val searchHistoryListFlow2 = MutableStateFlow<List<HistoryEntity>>(emptyList())
    fun getAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            var searchList = dbRepo.getAllHistory() ?: emptyList()
            searchHistoryListFlow2.value = searchList
        }
    }

    fun deleteAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepo.deleteAllHistory()
            delay(200).apply {
                getAllHistory()
            }
        }
    }
}
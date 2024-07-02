package com.lazydeveloper.data.repository

import com.lazydeveloper.database.dao.HistoryDao
import com.lazydeveloper.database.entities.HistoryEntity
import javax.inject.Inject

class DatabaseRepo @Inject constructor(
    private val historyDao: HistoryDao,
//    private val searchHistoryDao: SearchHistoryDao
) {
//    fun getSearchTags() = searchHistoryDao.allSearchTags()
//    fun checkDuplicate(searchTag: String) = searchHistoryDao.checkDuplicate(searchTag)
//    fun getRowCount() = searchHistoryDao.getRowCount()
//    fun deleteLastRow() = searchHistoryDao.deleteLastRow()
//    fun addSearchTag(item: SearchHistoryEntity) = searchHistoryDao.addSearchTag(item)
    fun getAllHistory() = historyDao.getAllHistory()
    fun addHistory(item: HistoryEntity) = historyDao.addHistory(item)
    fun checkDuplicateHistory(movieId: String) = historyDao.checkDuplicate(movieId)
    fun getHistoryRowCount() = historyDao.getRowCount()
    fun deleteLastHistory() = historyDao.deleteLastRow()
    fun deleteAllHistory() = historyDao.deleteAllHistory()
}
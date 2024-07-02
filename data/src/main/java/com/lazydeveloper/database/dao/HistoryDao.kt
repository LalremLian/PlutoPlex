package com.lazydeveloper.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lazydeveloper.database.entities.HistoryEntity

@Dao
interface HistoryDao {
    
    @Query("select * from history order by id desc")
    fun getAllHistory(): List<HistoryEntity>?
    
    @Insert
    fun addHistory(item: HistoryEntity)
    
    @Query("select movieId from history where movieId = :movieId")
    fun checkDuplicate(movieId: String): List<String>
    
    @Query("delete from history where id = (select min(id) from history)")
    fun deleteLastRow()
    
    @Query("select count(*) from history")
    fun getRowCount(): Int

    @Query("DELETE FROM history")
    fun deleteAllHistory()
}
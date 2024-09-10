package com.lazydeveloper.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lazydeveloper.database.entities.SearchHistoryEntity

@Dao
interface SearchHistoryDao {
    
    @Query("select * from search_history order by id desc")
    fun allSearchTags(): List<SearchHistoryEntity>?
    
    @Query("select searchTag from search_history where searchTag = :searchTag")
    fun checkDuplicate(searchTag: String): List<String>
    
    @Query("delete from search_history where id = (select min(id) from search_history)")
    fun deleteLastRow()
    
    @Query("SELECT COUNT(*) FROM search_history")
    fun getRowCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSearchTag(item: SearchHistoryEntity)
}
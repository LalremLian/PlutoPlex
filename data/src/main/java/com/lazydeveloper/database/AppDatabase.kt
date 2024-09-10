package com.lazydeveloper.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lazydeveloper.database.dao.HistoryDao
import com.lazydeveloper.database.dao.SearchHistoryDao
import com.lazydeveloper.database.entities.HistoryEntity
import com.lazydeveloper.database.entities.SearchHistoryEntity

@Database(
    entities = [
        SearchHistoryEntity::class,
        HistoryEntity::class
    ],
    version = 1,
    exportSchema = true,
//    autoMigrations = [
//        AutoMigration(
//            from = 1,
//            to = 2,
//    //            spec = AppDatabase.DbMigrationSpec::class //This spec only needs in case of RenameColumn, RenameTable, DeleteColumn and DeleteTable. Otherwise room will automatically migrate database, no need to add specs here.
//        ),
//    ]
)
abstract class AppDatabase : RoomDatabase() {

//    @RenameColumn.Entries(
//        RenameColumn(tableName = "SearchHistoryEntity", fromColumnName = "id", toColumnName = "columnId"),
//        RenameColumn(tableName = "SearchHistoryEntity", fromColumnName = "searchTag", toColumnName = "searchTagNew"),
//    )
//    @DeleteTable(tableName = "HistoryEntity")
//    class DbMigrationSpec : AutoMigrationSpec

    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun historyDao(): HistoryDao
}
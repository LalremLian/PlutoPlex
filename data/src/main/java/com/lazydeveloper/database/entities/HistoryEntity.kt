package com.lazydeveloper.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var movieId: String,
    var movieName: String,
    var type: String,
    var watchDate: String,
    var poster: String,
    var year: String,
    var region: String,
    var genre: String,
)
package com.lazydeveloper.di

import android.content.Context
import androidx.room.Room
import com.lazydeveloper.database.AppDatabase
import com.lazydeveloper.database.dao.HistoryDao
import com.lazydeveloper.database.dao.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "CLVPLEX_DB")
//            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    @Singleton
    fun providesHistoryDao(db: AppDatabase): HistoryDao {
        return db.historyDao()
    }
    
    @Provides
    @Singleton
    fun providesSearchHistoryDao(db: AppDatabase): SearchHistoryDao {
        return db.searchHistoryDao()
    }
    
}
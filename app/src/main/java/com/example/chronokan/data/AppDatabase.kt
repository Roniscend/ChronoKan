package com.example.chronokan.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chronokan.data.local.dao.CardDao
import com.example.chronokan.data.local.dao.HistoryDao
import com.example.chronokan.data.entity.CardEntity
import com.example.chronokan.data.entity.HistoryActionEntity

@Database(
    entities = [CardEntity::class, HistoryActionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun historyDao(): HistoryDao
}
package com.example.chronokan.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.chronokan.data.entity.CardEntity
import com.example.chronokan.data.entity.HistoryActionEntity
import com.example.chronokan.data.local.converters.CommandConverter
import com.example.chronokan.data.local.dao.CardDao
import com.example.chronokan.data.local.dao.HistoryDao

@Database(
    entities = [CardEntity::class, HistoryActionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CommandConverter::class)
abstract class ChronosDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun historyDao(): HistoryDao
}
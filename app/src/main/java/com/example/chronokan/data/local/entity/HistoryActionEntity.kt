package com.example.chronokan.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_actions")
data class HistoryActionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Must be named 'id'
    val actionType: String,
    val cardId: Int,
    val timestamp: Long,
    // Add other fields you are using
)
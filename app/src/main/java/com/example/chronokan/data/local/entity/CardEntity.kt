package com.example.chronokan.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val columnId: Int, // 0 = To Do, 1 = In Progress, 2 = Done
    val title: String,
    val description: String,
    val position: Int // Order within the column
)
package com.example.chronokan.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson

class CommandConverter {
    @TypeConverter
    fun fromString(value: String): Map<String, String> {
        return Gson().fromJson(value, Map::class.java) as Map<String, String>
    }

    @TypeConverter
    fun fromMap(map: Map<String, String>): String {
        return Gson().toJson(map)
    }
}
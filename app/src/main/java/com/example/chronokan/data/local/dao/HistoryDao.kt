package com.example.chronokan.data.local.dao

import androidx.room.*
import com.example.chronokan.data.entity.HistoryActionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history_actions ORDER BY timestamp ASC")
    fun getHistoryTimeline(): Flow<List<HistoryActionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: HistoryActionEntity)

    // Used when a user performs a new action while in the middle of a "time travel"
    @Query("DELETE FROM history_actions WHERE id > :lastActionId")
    suspend fun deleteFutureActions(lastActionId: Int)

    @Query("DELETE FROM history_actions")
    suspend fun clearHistory()
}
package com.example.chronokan.data.local.dao // Change this line
import androidx.room.*
import com.example.chronokan.data.entity.CardEntity
import kotlinx.coroutines.flow.Flow // Ensure this import is exactly this

@Dao // <--- CRITICAL: This must be present
interface CardDao {

    @Query("SELECT * FROM cards ORDER BY position ASC")
    fun getAllCards(): Flow<List<CardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CardEntity)

    @Update
    suspend fun updateCard(card: CardEntity)

    @Delete
    suspend fun deleteCard(card: CardEntity)

    // Check that 'columnId', 'position', and 'id' match your CardEntity exactly
    @Query("UPDATE cards SET columnId = :col, position = :pos WHERE id = :cardId")
    suspend fun updateCardLocation(cardId: Int, col: Int, pos: Int)

    // Check that 'title' and 'description' match your CardEntity exactly
    @Query("UPDATE cards SET title = :title, description = :description WHERE id = :cardId")
    suspend fun updateCardText(cardId: Int, title: String, description: String)
}
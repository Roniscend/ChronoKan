package com.example.chronokan.data.repository

import com.example.chronokan.data.local.dao.CardDao
import com.example.chronokan.data.local.dao.HistoryDao
import com.example.chronokan.data.entity.CardEntity
import com.example.chronokan.data.entity.HistoryActionEntity
import kotlinx.coroutines.flow.Flow

class KanbanRepository(
    private val cardDao: CardDao,
    private val historyDao: HistoryDao // ADDED THIS
) {
    // Card Operations
    val allCards: Flow<List<CardEntity>> = cardDao.getAllCards()
    suspend fun insertCard(card: CardEntity) = cardDao.insertCard(card)
    suspend fun updateCard(card: CardEntity) = cardDao.updateCard(card)
    suspend fun deleteCard(card: CardEntity) = cardDao.deleteCard(card)
    suspend fun updateCardLocation(cardId: Int, col: Int, pos: Int) = cardDao.updateCardLocation(cardId, col, pos)
    suspend fun updateCardText(cardId: Int, title: String, description: String) = cardDao.updateCardText(cardId, title, description)

    // History Operations
    val historyTimeline: Flow<List<HistoryActionEntity>> = historyDao.getHistoryTimeline()
    suspend fun insertHistoryAction(action: HistoryActionEntity) = historyDao.insertAction(action)
    suspend fun deleteFutureActions(lastActionId: Int) = historyDao.deleteFutureActions(lastActionId)
}
package com.example.chronokan.domain.usecase

import com.example.chronokan.data.entity.CardEntity
import com.example.chronokan.domain.model.HistoryNode
import com.example.chronokan.domain.model.KanbanAction

class GetBoardStateAtTimeUseCase {
    /**
     * Replays actions to generate a specific point in time.
     * This allows the user to jump across history instantly.
     */
    fun execute(initialCards: List<CardEntity>, history: List<HistoryNode>, targetIndex: Int): List<CardEntity> {
        var currentState = initialCards.toMutableList()

        for (i in 0..targetIndex) {
            val action = history[i].action
            // Apply the forward logic of the action to the currentState list
            // ... (Logic for Move, Add, Edit)
        }
        return currentState
    }
}
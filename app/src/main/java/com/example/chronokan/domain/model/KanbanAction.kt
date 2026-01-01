package com.example.chronokan.domain.model

sealed class KanbanAction {

    // Triggered when a card is dragged between columns or reordered
    data class Move(
        val cardId: Int,
        val fromCol: Int,
        val toCol: Int,
        val fromPos: Int,
        val toPos: Int
    ) : KanbanAction()

    // Triggered when the Task Detail screen is saved
    data class Edit(
        val cardId: Int,
        val oldTitle: String,
        val newTitle: String,
        val oldDesc: String,
        val newDesc: String
    ) : KanbanAction()

    // Triggered when the Floating Action Button is used
    data class Add(
        val cardId: Int,
        val title: String,
        val colId: Int
    ) : KanbanAction()

    // Added: Triggered if you implement a "Swipe to Delete" feature
    // Storing old values allows the Time Machine to "Restore" the card
    data class Delete(
        val cardId: Int,
        val oldTitle: String,
        val oldDesc: String,
        val oldColId: Int,
        val oldPos: Int
    ) : KanbanAction()
}
package com.example.chronokan.domain.model

data class HistoryNode(
    val action: KanbanAction,
    var prev: HistoryNode? = null,
    var next: HistoryNode? = null
)
package com.example.chronokan.core.dsa

import com.example.chronokan.domain.model.HistoryNode
import com.example.chronokan.domain.model.KanbanAction

class HistoryManager {
    private var head: HistoryNode? = null
    private var current: HistoryNode? = null
    private var totalSize = 0

    fun addAction(action: KanbanAction) {
        val newNode = HistoryNode(action)
        if (head == null) {
            head = newNode
            current = newNode
        } else {
            // Truncate the "future" if we add a new action while in the past
            current?.next = newNode
            newNode.prev = current
            current = newNode
        }
        updateSize()
    }

    fun undo(): KanbanAction? {
        val action = current?.action
        current = current?.prev
        return action
    }

    fun redo(): KanbanAction? {
        val nextNode = current?.next ?: head // If current is null (before head), redo moves to head
        current = nextNode
        return current?.action
    }

    fun getCurrentPosition(): Int {
        var count = 0
        var temp = head
        while (temp != null && temp != current) {
            count++
            temp = temp.next
        }
        return if (current == null) 0 else count + 1
    }

    fun getTimelineSize(): Int = totalSize

    private fun updateSize() {
        var count = 0
        var temp = head
        while (temp != null) {
            count++
            temp = temp.next
        }
        totalSize = count
    }
}
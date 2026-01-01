package com.example.chronokan.ui.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chronokan.core.dsa.HistoryManager
import com.example.chronokan.data.entity.CardEntity
import com.example.chronokan.data.repository.KanbanRepository
import com.example.chronokan.domain.model.KanbanAction
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class BoardViewModel(private val repository: KanbanRepository) : ViewModel() {

    private val historyManager = HistoryManager()
    private val actionMutex = Mutex()
    private var scrubJob: Job? = null

    // Observe cards from repository
    val allCards: StateFlow<List<CardEntity>> = repository.allCards
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _totalSteps = MutableStateFlow(0)
    val totalSteps: StateFlow<Int> = _totalSteps.asStateFlow()

    // --- NEW: Added this function to fix your TaskDetailScreen error ---
    fun updateTaskDetails(cardId: Int, newTitle: String, newDesc: String) {
        val oldCard = allCards.value.find { it.id == cardId } ?: return
        val action = KanbanAction.Edit(
            cardId = cardId,
            oldTitle = oldCard.title,
            newTitle = newTitle,
            oldDesc = oldCard.description,
            newDesc = newDesc
        )
        executeNewAction(action)
    }

    fun addTask(title: String, description: String) {
        val newId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
        val action = KanbanAction.Add(
            cardId = newId,
            title = title,
            colId = 0
        )

        viewModelScope.launch {
            actionMutex.withLock {
                repository.insertCard(
                    CardEntity(
                        id = newId,
                        columnId = 0,
                        title = title,
                        description = description,
                        position = 0
                    )
                )
                historyManager.addAction(action)
                syncPointers()
            }
        }
    }

    fun moveCard(cardId: Int, fromCol: Int, toCol: Int) {
        val action = KanbanAction.Move(cardId, fromCol, toCol, 0, 0)
        executeNewAction(action)
    }

    fun requestDeleteCard(card: CardEntity) {
        val action = KanbanAction.Delete(
            cardId = card.id,
            oldTitle = card.title,
            oldDesc = card.description,
            oldColId = card.columnId,
            oldPos = card.position
        )
        executeNewAction(action)
    }

    private fun executeNewAction(action: KanbanAction) {
        viewModelScope.launch {
            actionMutex.withLock {
                applyAction(action, isUndo = false)
                historyManager.addAction(action)
                syncPointers()
            }
        }
    }

    fun scrubToStep(targetIndex: Int) {
        scrubJob?.cancel()
        scrubJob = viewModelScope.launch {
            delay(16)
            actionMutex.withLock {
                var currentPos = historyManager.getCurrentPosition()
                while (currentPos != targetIndex) {
                    if (targetIndex < currentPos) {
                        historyManager.undo()?.let { applyAction(it, isUndo = true) }
                        currentPos--
                    } else {
                        historyManager.redo()?.let { applyAction(it, isUndo = false) }
                        currentPos++
                    }
                    _currentIndex.value = currentPos
                }
                _totalSteps.value = historyManager.getTimelineSize()
            }
        }
    }

    private suspend fun applyAction(action: KanbanAction, isUndo: Boolean) {
        when (action) {
            is KanbanAction.Move -> {
                val col = if (isUndo) action.fromCol else action.toCol
                val pos = if (isUndo) action.fromPos else action.toPos
                repository.updateCardLocation(action.cardId, col, pos)
            }
            is KanbanAction.Edit -> {
                val title = if (isUndo) action.oldTitle else action.newTitle
                val desc = if (isUndo) action.oldDesc else action.newDesc
                // Physical update to the database
                repository.updateCardText(action.cardId, title, desc)
            }
            is KanbanAction.Add -> {
                if (isUndo) {
                    allCards.value.find { it.id == action.cardId }?.let { repository.deleteCard(it) }
                } else {
                    repository.insertCard(
                        CardEntity(id = action.cardId, columnId = action.colId, title = action.title, description = "", position = 0)
                    )
                }
            }
            is KanbanAction.Delete -> {
                if (isUndo) {
                    repository.insertCard(
                        CardEntity(
                            id = action.cardId,
                            title = action.oldTitle,
                            description = action.oldDesc,
                            columnId = action.oldColId,
                            position = action.oldPos
                        )
                    )
                } else {
                    allCards.value.find { it.id == action.cardId }?.let { repository.deleteCard(it) }
                }
            }
        }
    }

    private fun syncPointers() {
        _currentIndex.value = historyManager.getCurrentPosition()
        _totalSteps.value = historyManager.getTimelineSize()
    }
}
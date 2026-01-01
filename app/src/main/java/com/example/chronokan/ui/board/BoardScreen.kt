package com.example.chronokan.ui.board

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chronokan.ui.board.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(viewModel: BoardViewModel, onNavigateToTask: (Int) -> Unit) {
    // Collect state safely using lifecycle-aware collection
    val cards by viewModel.allCards.collectAsStateWithLifecycle()
    val currentIndex by viewModel.currentIndex.collectAsStateWithLifecycle()
    val totalSteps by viewModel.totalSteps.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("CHRONOS BOARD") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        bottomBar = {
            // Surface adds a visual lift and prevents UI leaking
            Surface(
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                HistoryScrubber(
                    currentStep = currentIndex,
                    totalSteps = totalSteps,
                    onScrub = viewModel::scrubToStep
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                // Column 0: Todo
                ColumnView(
                    columnName = "Todo",
                    cards = cards.filter { it.columnId == 0 },
                    onCardClick = onNavigateToTask
                )

                // Column 1: Progress
                ColumnView(
                    columnName = "Progress",
                    cards = cards.filter { it.columnId == 1 },
                    onCardClick = onNavigateToTask
                )

                // Column 2: Done
                ColumnView(
                    columnName = "Done",
                    cards = cards.filter { it.columnId == 2 },
                    onCardClick = onNavigateToTask
                )
            }
        }

        if (showAddDialog) {
            // Ensure AddTaskDialog is imported correctly from components
            AddTaskDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, description ->
                    viewModel.addTask(title, description)
                    showAddDialog = false
                }
            )
        }
    }
}
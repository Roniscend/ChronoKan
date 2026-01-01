package com.example.chronokan.ui.board

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TaskListScreen(
    columnId: Int,
    viewModel: BoardViewModel,
    onNavigateToTask: (Int) -> Unit
) {
    // Observe the global list of cards from the ViewModel
    val cards by viewModel.allCards.collectAsState()

    // Filter cards belonging to this specific screen (Todo=0, Progress=1, Done=2)
    val filteredCards = cards.filter { it.columnId == columnId }

    Column(modifier = Modifier.fillMaxSize()) {
        if (filteredCards.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No tasks here yet.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredCards, key = { it.id }) { card ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onNavigateToTask(card.id) }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = card.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                if (card.description.isNotEmpty()) {
                                    Text(
                                        text = card.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 1,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Action Buttons based on the screen/column
                            Row {
                                if (columnId == 2) {
                                    // LOGIC FOR DONE SCREEN (Column 2)
                                    // 1. Move Back to Progress
                                    IconButton(onClick = {
                                        viewModel.moveCard(card.id, columnId, 1)
                                    }) {
                                        Icon(
                                            Icons.Default.ArrowBack,
                                            contentDescription = "Back to Progress"
                                        )
                                    }

                                    // 2. Mark as Completed (Green Tick)
                                    IconButton(onClick = {
                                        // You can add a specific completion logic in ViewModel here later
                                        // For now, it stays in Done but shows the action is finished
                                    }) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Complete",
                                            tint = Color(0xFF4CAF50) // Material Green
                                        )
                                    }

                                    // 3. Delete Task (Trash Icon)
                                    IconButton(onClick = {
                                        viewModel.requestDeleteCard(card)
                                    }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                } else {
                                    // LOGIC FOR TODO (0) AND PROGRESS (1)
                                    if (columnId > 0) {
                                        IconButton(onClick = {
                                            viewModel.moveCard(card.id, columnId, columnId - 1)
                                        }) {
                                            Icon(Icons.Default.ArrowBack, contentDescription = "Move Back")
                                        }
                                    }

                                    if (columnId < 2) {
                                        IconButton(onClick = {
                                            viewModel.moveCard(card.id, columnId, columnId + 1)
                                        }) {
                                            Icon(Icons.Default.ArrowForward, contentDescription = "Move Forward")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
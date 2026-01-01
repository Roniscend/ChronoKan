package com.example.chronokan.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.chronokan.ui.auth.AuthViewModel
import com.example.chronokan.ui.auth.ProfileScreen // Ensure this is created
import com.example.chronokan.ui.board.BoardViewModel
import com.example.chronokan.ui.board.TaskListScreen
import com.example.chronokan.ui.board.components.HistoryScrubber
import com.example.chronokan.ui.details.TaskDetailScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChronosNavGraph(
    viewModel: BoardViewModel,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    val currentIndex by viewModel.currentIndex.collectAsState()
    val totalSteps by viewModel.totalSteps.collectAsState()

    var showMenu by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    var newTaskTitle by remember { mutableStateOf("") }
    var newTaskDescription by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("CHRONOS") },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.Menu, contentDescription = "Settings")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        // UPDATED: Navigate to Profile
                        DropdownMenuItem(
                            text = { Text("View Profile") },
                            onClick = {
                                showMenu = false
                                navController.navigate("profile")
                            },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Completed Tasks") },
                            onClick = { showMenu = false },
                            leadingIcon = { Icon(Icons.Default.CheckCircle, contentDescription = null) }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        DropdownMenuItem(
                            text = { Text("Logout", color = Color.Red) },
                            onClick = {
                                showMenu = false
                                authViewModel.signOut()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.ExitToApp,
                                    contentDescription = "Logout",
                                    tint = Color.Red
                                )
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    newTaskTitle = ""
                    newTaskDescription = ""
                    showAddDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // HIDE Bottom Bar when on Profile or Detail screens for a cleaner look
            val hideBottomBarRoutes = listOf("profile", "task_detail")
            val currentRoute = currentDestination?.route ?: ""

            if (hideBottomBarRoutes.none { currentRoute.startsWith(it) }) {
                Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                    HistoryScrubber(
                        currentStep = currentIndex,
                        totalSteps = totalSteps,
                        onScrub = { step -> viewModel.scrubToStep(step) }
                    )

                    NavigationBar {
                        val navItems = listOf(
                            Triple("todo", Icons.Default.List, "Todo"),
                            Triple("progress", Icons.Default.Refresh, "Progress"),
                            Triple("done", Icons.Default.Done, "Done")
                        )

                        navItems.forEach { (route, icon, label) ->
                            NavigationBarItem(
                                icon = { Icon(icon, contentDescription = label) },
                                label = { Text(label) },
                                selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                                onClick = {
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "todo",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("todo") {
                TaskListScreen(0, viewModel) { id -> navController.navigate("task_detail/$id") }
            }
            composable("progress") {
                TaskListScreen(1, viewModel) { id -> navController.navigate("task_detail/$id") }
            }
            composable("done") {
                TaskListScreen(2, viewModel) { id -> navController.navigate("task_detail/$id") }
            }
            // NEW: Profile Route
            composable("profile") {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = "task_detail/{taskId}",
                arguments = listOf(navArgument("taskId") { type = NavType.IntType })
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
                TaskDetailScreen(taskId, viewModel) { navController.popBackStack() }
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Create New Task") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = newTaskTitle,
                            onValueChange = { newTaskTitle = it },
                            label = { Text("Task Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = newTaskDescription,
                            onValueChange = { newTaskDescription = it },
                            label = { Text("Task Details") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )
                    }
                },
                confirmButton = {
                    Button(
                        enabled = newTaskTitle.isNotBlank(),
                        onClick = {
                            viewModel.addTask(newTaskTitle, newTaskDescription)
                            showAddDialog = false
                        }
                    ) { Text("Create") }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}
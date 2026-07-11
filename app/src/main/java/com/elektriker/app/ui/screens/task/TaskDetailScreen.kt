package com.elektriker.app.ui.screens.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.elektriker.app.data.local.entity.WorkStepEntity
import com.elektriker.app.data.local.entity.WorkTaskEntity
import com.elektriker.app.ui.components.BottomNavBar
import com.elektriker.app.ui.components.StepChecklistItem
import com.elektriker.app.ui.components.WarningCard
import com.elektriker.app.ui.navigation.Screen
import com.elektriker.app.ui.theme.GreenSuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: String,
    navController: NavController,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val task by viewModel.task.collectAsStateWithLifecycle()
    val steps by viewModel.steps.collectAsStateWithLifecycle()
    val warnings by viewModel.warnings.collectAsStateWithLifecycle()

    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(task?.title ?: "Laden...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        @Suppress("DEPRECATION")
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = "home",
                onNavigate = { route ->
                    when (route) {
                        "home" -> navController.navigate(Screen.Home.route) { popUpTo(Screen.Home.route) { inclusive = true } }
                        "history" -> navController.navigate(Screen.History.route)
                        "new_task" -> navController.navigate(Screen.NewTask.route)
                        "knowledge" -> navController.navigate(Screen.Knowledge.route)
                        "profile" -> navController.navigate(Screen.Profile.route)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            task?.let { t ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = t.category,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                if (t.isCompleted) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = "Erledigt",
                                        tint = GreenSuccess
                                    )
                                }
                            }
                            if (t.location.isNotBlank()) {
                                Text(
                                    text = t.location,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            if (t.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = t.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                if (warnings.isNotEmpty()) {
                    item {
                        Text(
                            text = "Erfahrungs-Warnungen",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    items(warnings) { warning ->
                        WarningCard(
                            title = "Fehler bei ${warning.taskCategory}",
                            description = warning.description,
                            severity = warning.severity
                        )
                    }
                }

                if (steps.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Schritte",
                                style = MaterialTheme.typography.titleLarge
                            )
                            FilledTonalButton(
                                onClick = {
                                    navController.navigate(Screen.Assistant.createRoute(taskId))
                                }
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Assistent")
                            }
                        }
                    }

                    items(steps) { step ->
                        StepChecklistItem(
                            stepNumber = step.stepOrder + 1,
                            description = step.description,
                            isDone = step.isDone,
                            warning = step.warning,
                            onToggle = { }
                        )
                    }
                }
            }
        }
    }
}

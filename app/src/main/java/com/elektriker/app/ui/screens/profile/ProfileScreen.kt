package com.elektriker.app.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.elektriker.app.ui.components.BottomNavBar
import com.elektriker.app.ui.navigation.Screen
import com.elektriker.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil & Statistik") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = "profile",
                onNavigate = { route ->
                    when (route) {
                        "home" -> navController.navigate(Screen.Home.route) { popUpTo(Screen.Home.route) { inclusive = true } }
                        "projects" -> navController.navigate(Screen.Projects.route)
                        "new_task" -> navController.navigate(Screen.NewTask.route)
                        "knowledge" -> navController.navigate(Screen.Knowledge.route)
                        "profile" -> {}
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Deine Statistik",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Abgeschlossen",
                        value = "${state.completedTasks}",
                        icon = Icons.Default.CheckCircle,
                        color = GreenSuccess,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Erledigungsrate",
                        value = "${(state.completionRate * 100).toInt()}%",
                        icon = Icons.Default.TrendingUp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Arbeiten",
                        value = "${state.totalTasks}",
                        icon = Icons.Default.Work,
                        color = BlueInfo,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Fehler",
                        value = "${state.errorCount}",
                        icon = Icons.Default.BugReport,
                        color = if (state.errorCount > 0) ErrorSeverityHigh else GreenSuccess,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Text(
                    text = "Arbeiten nach Kategorie",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (state.tasksByCategory.isEmpty()) {
                item {
                    Text(
                        text = "Noch keine Daten vorhanden",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(state.tasksByCategory.toList()) { (category, count) ->
                    CategoryBar(category = category, count = count, maxCount = state.tasksByCategory.values.maxOrNull() ?: 1)
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Letzte Fehler",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (state.recentErrors.isEmpty()) {
                item {
                    Text(
                        text = "Keine Fehler dokumentiert",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(state.recentErrors) { error ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = ErrorSeverityHigh.copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = ErrorSeverityHigh,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = { navController.navigate(Screen.Skills.route) },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Skills & Erfahrung")
                }
            }

            item {
                Button(
                    onClick = { navController.navigate(Screen.Achievements.route) },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Erfolge")
                }
            }

            item {
                OutlinedButton(
                    onClick = { navController.navigate(Screen.ErrorHistory.route) },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.List, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Alle Fehler anzeigen")
                }
            }

            item {
                OutlinedButton(
                    onClick = { navController.navigate(Screen.Insights.route) },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.Insights, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Muster & Erkenntnisse")
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.displaySmall,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CategoryBar(
    category: String,
    count: Int,
    maxCount: Int
) {
    val fraction = count.toFloat() / maxCount.toFloat()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "$count",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

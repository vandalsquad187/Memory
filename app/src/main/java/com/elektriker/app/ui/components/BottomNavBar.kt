package com.elektriker.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { onNavigate("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentRoute == "history",
            onClick = { onNavigate("history") },
            icon = { Icon(Icons.Default.History, contentDescription = "Verlauf") },
            label = { Text("Verlauf") }
        )
        NavigationBarItem(
            selected = currentRoute == "new_task",
            onClick = { onNavigate("new_task") },
            icon = {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Neue Arbeit",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            label = {
                Text(
                    "Arbeit",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == "knowledge",
            onClick = { onNavigate("knowledge") },
            icon = { Icon(Icons.Default.Book, contentDescription = "Wissen") },
            label = { Text("Wissen") }
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { onNavigate("profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
            label = { Text("Profil") }
        )
    }
}

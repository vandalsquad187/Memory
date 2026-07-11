package com.elektriker.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimerWidget(
    elapsedSeconds: Int,
    isRunning: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val timeString = String.format("%02d:%02d", minutes, seconds)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = timeString,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!isRunning) {
                    Button(
                        onClick = onStart,
                        modifier = Modifier.height(56.dp).width(120.dp)
                    ) {
                        Text("Start", style = MaterialTheme.typography.labelLarge)
                    }
                } else {
                    Button(
                        onClick = onPause,
                        modifier = Modifier.height(56.dp).width(120.dp)
                    ) {
                        Text("Pause", style = MaterialTheme.typography.labelLarge)
                    }
                }

                if (elapsedSeconds > 0) {
                    OutlinedButton(
                        onClick = onReset,
                        modifier = Modifier.height(56.dp).width(120.dp)
                    ) {
                        Text("Reset", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

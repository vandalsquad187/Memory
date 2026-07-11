package com.elektriker.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elektriker.app.ui.screens.assistant.AssistantScreen
import com.elektriker.app.ui.screens.history.HistoryScreen
import com.elektriker.app.ui.screens.home.HomeScreen
import com.elektriker.app.ui.screens.knowledge.KnowledgeScreen
import com.elektriker.app.ui.screens.profile.ProfileScreen
import com.elektriker.app.ui.screens.task.NewTaskScreen
import com.elektriker.app.ui.screens.task.TaskDetailScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = androidx.navigation.compose.rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.NewTask.route) {
            NewTaskScreen(navController = navController)
        }

        composable(
            route = Screen.TaskDetail.route,
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: return@composable
            TaskDetailScreen(
                taskId = taskId,
                navController = navController
            )
        }

        composable(
            route = Screen.Assistant.route,
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: return@composable
            AssistantScreen(
                taskId = taskId,
                navController = navController
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(navController = navController)
        }

        composable(Screen.Knowledge.route) {
            KnowledgeScreen(navController = navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}

package com.elektriker.app.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object NewTask : Screen("new_task")
    data object TaskDetail : Screen("task_detail/{taskId}") {
        fun createRoute(taskId: String) = "task_detail/$taskId"
    }
    data object Assistant : Screen("assistant/{taskId}") {
        fun createRoute(taskId: String) = "assistant/$taskId"
    }
    data object History : Screen("history")
    data object Knowledge : Screen("knowledge")
    data object KnowledgeDetail : Screen("knowledge/{entryId}") {
        fun createRoute(entryId: String) = "knowledge/$entryId"
    }
    data object Profile : Screen("profile")
    data object TemplatePicker : Screen("template_picker")
}

package com.elektriker.app.service

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.elektriker.app.data.local.dao.*
import com.elektriker.app.data.local.entity.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val workTaskDao: WorkTaskDao,
    private val workStepDao: WorkStepDao,
    private val errorLogDao: ErrorLogDao,
    private val errorCauseDao: ErrorCauseDao,
    private val knowledgeBaseDao: KnowledgeBaseDao,
    private val projectDao: ProjectDao,
    private val skillDao: SkillDao,
    private val achievementDao: AchievementDao,
    private val workflowTemplateDao: WorkflowTemplateDao,
    private val photoDao: PhotoDao
) {

    suspend fun exportBackup(): File? {
        val backup = JSONObject()

        backup.put("app", "GesellenGedächtnis")
        backup.put("exportDate", System.currentTimeMillis())
        backup.put("version", 1)

        backup.put("tasks", toJsonArray(workTaskDao.getAllTasks().first()) { it.toJson() })
        backup.put("steps", toJsonArray(workStepDao.getAllSteps().first()) { it.toJson() })
        backup.put("errors", toJsonArray(errorLogDao.getAllErrors().first()) { it.toJson() })
        backup.put("errorCauses", toJsonArray(errorCauseDao.getAllCauses().first()) { it.toJson() })
        backup.put("knowledge", toJsonArray(knowledgeBaseDao.getAllEntries().first()) { it.toJson() })
        backup.put("projects", toJsonArray(projectDao.getAllProjects().first()) { it.toJson() })
        backup.put("skills", toJsonArray(skillDao.getAllSkills().first()) { it.toJson() })
        backup.put("achievements", toJsonArray(achievementDao.getAllAchievements().first()) { it.toJson() })
        backup.put("templates", toJsonArray(workflowTemplateDao.getAllTemplates().first()) { it.toJson() })

        val dateStr = SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.GERMANY).format(Date())
        val file = File(context.cacheDir, "GesellenGedaechtnis_Backup_$dateStr.json")
        file.writeText(backup.toString(2))
        return file
    }

    fun shareBackup(file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "Backup teilen"))
    }

    private fun <T> toJsonArray(items: List<T>, transform: (T) -> JSONObject): JSONArray {
        val arr = JSONArray()
        items.forEach { arr.put(transform(it)) }
        return arr
    }

    @Suppress("DEPRECATION")
    private fun WorkTaskEntity.toJson() = JSONObject().apply {
        put("id", id)
        put("title", title)
        put("category", category)
        put("description", description)
        put("location", location)
        put("customerName", customerName)
        put("date", date)
        put("isCompleted", isCompleted)
        put("projectId", projectId ?: JSONObject.NULL)
        put("durationSeconds", durationSeconds)
        put("materials", materials)
        put("tools", tools)
        put("solution", solution)
        put("rating", rating)
    }

    @Suppress("DEPRECATION")
    private fun WorkStepEntity.toJson() = JSONObject().apply {
        put("id", id)
        put("taskId", taskId)
        put("stepOrder", stepOrder)
        put("description", description)
        put("isDone", isDone)
        put("warning", warning ?: JSONObject.NULL)
        put("durationSeconds", durationSeconds)
    }

    @Suppress("DEPRECATION")
    private fun ErrorLogEntity.toJson() = JSONObject().apply {
        put("id", id)
        put("taskCategory", taskCategory)
        put("description", description)
        put("severity", severity)
        put("date", date)
        put("taskId", taskId ?: JSONObject.NULL)
        put("wasAvoided", wasAvoided)
        put("causes", causes)
        put("causeIds", causeIds)
        put("solution", solution)
    }

    @Suppress("DEPRECATION")
    private fun ErrorCauseEntity.toJson() = JSONObject().apply {
        put("id", id)
        put("label", label)
        put("description", description)
        put("category", category)
    }

    @Suppress("DEPRECATION")
    private fun KnowledgeBaseEntity.toJson() = JSONObject().apply {
        put("id", id)
        put("title", title)
        put("content", content)
        put("tags", tags)
        put("category", category)
        put("isFavorite", isFavorite)
        put("createdAt", createdAt)
        put("updatedAt", updatedAt)
    }

    @Suppress("DEPRECATION")
    private fun ProjectEntity.toJson() = JSONObject().apply {
        put("id", id)
        put("name", name)
        put("address", address)
        put("contactName", contactName)
        put("contactPhone", contactPhone)
        put("createdAt", createdAt)
    }

    @Suppress("DEPRECATION")
    private fun SkillEntity.toJson() = JSONObject().apply {
        put("id", id)
        put("name", name)
        put("category", category)
        put("description", description)
        put("currentXp", currentXp)
        put("level", level)
        put("maxLevel", maxLevel)
        put("nextLevelXp", nextLevelXp)
    }

    @Suppress("DEPRECATION")
    private fun AchievementEntity.toJson() = JSONObject().apply {
        put("id", id)
        put("name", name)
        put("description", description)
        put("tier", tier)
        put("isUnlocked", isUnlocked)
        put("unlockedAt", unlockedAt ?: JSONObject.NULL)
    }

    @Suppress("DEPRECATION")
    private fun WorkflowTemplateEntity.toJson() = JSONObject().apply {
        put("id", id)
        put("name", name)
        put("category", category)
        put("stepsJson", stepsJson)
        put("isBuiltIn", isBuiltIn)
        put("usageCount", usageCount)
        put("createdAt", createdAt)
    }
}

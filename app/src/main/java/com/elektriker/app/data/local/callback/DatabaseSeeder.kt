package com.elektriker.app.data.local.callback

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.elektriker.app.data.local.AppDatabase
import com.elektriker.app.data.local.entity.WorkflowTemplateEntity
import com.elektriker.app.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class DatabaseSeeder(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch {
            seedData(db)
        }
    }

    private suspend fun seedData(db: SupportSQLiteDatabase) {
        val now = System.currentTimeMillis()

        db.execSQL(
            """INSERT OR IGNORE INTO workflow_templates (id, name, category, stepsJson, isBuiltIn, usageCount, createdAt)
               VALUES ('${UUID.randomUUID()}', 'Unterverteilung verdrahten', '${Constants.Categories.UV}',
               '${Constants.BuiltInTemplates.uvVerdrahten.joinToString("\n---\n")}', 1, 0, $now)"""
        )
        db.execSQL(
            """INSERT OR IGNORE INTO workflow_templates (id, name, category, stepsJson, isBuiltIn, usageCount, createdAt)
               VALUES ('${UUID.randomUUID()}', 'RCD/FI-Schalter prüfen', '${Constants.Categories.RCD}',
               '${Constants.BuiltInTemplates.rcdPruefen.joinToString("\n---\n")}', 1, 0, $now)"""
        )

        db.execSQL(
            """INSERT OR IGNORE INTO knowledge_base (id, title, content, tags, category, isFavorite, sourceTaskId, createdAt, updatedAt)
               VALUES ('${UUID.randomUUID()}', 'RCD-Auslösezeiten', 'Typ: 30mA ≤ 300ms\nTyp S: 30mA ≤ 500ms\nSelektiv: 300mA ≤ 40ms',
               'RCD, FI, Zeiten, Messung', '${Constants.Categories.RCD}', 1, NULL, $now, $now)"""
        )
        db.execSQL(
            """INSERT OR IGNORE INTO knowledge_base (id, title, content, tags, category, isFavorite, sourceTaskId, createdAt, updatedAt)
               VALUES ('${UUID.randomUUID()}', 'Farbcodierung Elektrik', 'PE: grün-gelb\nN: blau\nL1: braun\nL2: schwarz\nL3: grau',
               'Farben, Verdrahtung, PE, N, L1, L2, L3', '${Constants.Categories.VERDRAHTUNG}', 1, NULL, $now, $now)"""
        )
        db.execSQL(
            """INSERT OR IGNORE INTO knowledge_base (id, title, content, tags, category, isFavorite, sourceTaskId, createdAt, updatedAt)
               VALUES ('${UUID.randomUUID()}', 'Schleifenimpedanz Grenzwerte', '16A LS: ≤ 2,87Ω (TN)\n20A LS: ≤ 2,19Ω (TN)\n25A LS: ≤ 1,75Ω (TN)\n32A LS: ≤ 1,37Ω (TN)',
               'Schleifenimpedanz, Messung, Grenzwerte, LS', '${Constants.Categories.MESSUNG}', 1, NULL, $now, $now)"""
        )
    }
}

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

        seedErrorCauses(db)
        seedSkills(db)

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

    private suspend fun seedErrorCauses(db: SupportSQLiteDatabase) {
        val causes = listOf(
            "Spannung nicht geprüft" to "Vor Arbeitsbeginn Spannungsfreiheit messen",
            "Kabelquerschnitt falsch gewählt" to "Querschnitt an Last und Absicherung anpassen",
            "Adern vertauscht (N/PE/L)" to "Farbcodierung beachten: PE grün-gelb, N blau",
            "Sicherung nicht ausgeschaltet" to "Vor Arbeiten allpolig freischalten",
            "Werkzeug falsch verwendet" to "Geeignetes und geprüftes Werkzeug nutzen",
            "Vorschrift nicht beachtet" to "Aktuelle Normen und TAB einhalten",
            "Messung fehlerhaft durchgeführt" to "Messgerät prüfen und korrekt anschließen",
            "Bauteilbeschriftung übersehen" to "Typenschild und Anschlussplan lesen",
            "Dokumentation nicht gelesen" to "Bestandspläne und Herstellerangaben prüfen",
            "Zeitdruck / Hektik" to "Lieber einmal mehr kontrollieren",
            "Schutzart nicht beachtet" to "IP-Schutzart an Umgebung anpassen",
            "Leitungsschutz falsch dimensioniert" to "Auslösecharakteristik und Last prüfen"
        )
        causes.forEach { (label, desc) ->
            db.execSQL(
                """INSERT OR IGNORE INTO error_causes (id, label, description, category)
                   VALUES ('${uuid()}','${
                    label.replace("'", "''")
                }','${desc.replace("'", "''")}','')"""
            )
        }
    }

    private suspend fun seedSkills(db: SupportSQLiteDatabase) {
        val skills = listOf(
            Triple("UV-Verdrahtung", Constants.Categories.UV, "Unterverteilungen fachgerecht verdrahten und prüfen"),
            Triple("RCD-Prüfung", Constants.Categories.RCD, "FI-Schalter korrekt prüfen und dokumentieren"),
            Triple("Messung & Prüfung", Constants.Categories.MESSUNG, "Schleifenimpedanz, Isolationswiderstand und Co."),
            Triple("Schalter & Steckdosen", Constants.Categories.SCHALTER, "Installation und Prüfung von Schaltern und Steckdosen"),
            Triple("Sicherheitstechnik", Constants.Categories.SICHERHEIT, "Brandmelder, Alarmanlagen, Sicherheitsbeleuchtung"),
            Triple("Verdrahtung & Anschluss", Constants.Categories.VERDRAHTUNG, "Korrekte Verdrahtung von Geräten und Anlagen"),
            Triple("Fehlersuche & Diagnose", "", "Systematische Fehlersuche in elektrischen Anlagen"),
            Triple("Sicherheit & Vorschriften", "", "Arbeitssicherheit, TAB, DIN VDE Normen"),
            Triple("Beleuchtung", Constants.Categories.BELEUCHTUNG, "Beleuchtungsanlagen und Klimasteuerung"),
            Triple("Zähler & Netz", Constants.Categories.ZAEHLER, "Zähleranlagen und Netzanschluss"),
            Triple("Sonstiges", Constants.Categories.SONSTIGES, "Allgemeine Elektroarbeiten")
        )
        skills.forEach { (name, category, description) ->
            db.execSQL(
                """INSERT OR IGNORE INTO skills (id, name, category, description, iconName, currentXp, level, maxLevel, nextLevelXp)
                   VALUES ('${uuid()}','${name.replace("'", "''")}','$category','${description.replace("'", "''")}','',0,1,10,100)"""
            )
        }
    }

    private fun uuid() = UUID.randomUUID().toString()
}

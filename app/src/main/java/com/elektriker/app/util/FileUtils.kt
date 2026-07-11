package com.elektriker.app.util

import android.content.Context
import android.net.Uri
import java.io.File

object FileUtils {
    fun getPhotoDir(context: Context): File {
        val dir = File(context.filesDir, Constants.PHOTO_DIR)
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun getVoiceDir(context: Context): File {
        val dir = File(context.filesDir, Constants.VOICE_DIR)
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun getPdfDir(context: Context): File {
        val dir = File(context.filesDir, Constants.PDF_DIR)
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun createPhotoFile(context: Context): File {
        val dir = getPhotoDir(context)
        val name = "photo_${System.currentTimeMillis()}.jpg"
        return File(dir, name)
    }

    fun createVoiceFile(context: Context): File {
        val dir = getVoiceDir(context)
        val name = "voice_${System.currentTimeMillis()}.3gp"
        return File(dir, name)
    }
}

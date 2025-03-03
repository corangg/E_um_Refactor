package com.core.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

fun Context.downloadFont(fileUrl: String, fileName: String, extension: String): String? {
    return try {
        val cacheDir = this.cacheDir
        val file = File(cacheDir, "$fileName.$extension")

        if (!file.exists()) {
            val url = URL(fileUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val inputStream = connection.inputStream
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
        }

        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getFileFromPath(filePath: String?): File? {
    return try {
        if (filePath == "" || filePath == null) return null
        val file = File(filePath)
        return if (file.exists()) file else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

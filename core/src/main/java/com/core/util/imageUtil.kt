package com.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.uriToBitmap(imageUri: Uri?): Bitmap? {
    imageUri?: return null
    var inputStream: InputStream? = null
    return try {
        inputStream = contentResolver.openInputStream(imageUri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    } finally {
        inputStream?.close()
    }
}

fun Context.bitmapToUri( bitmap: Bitmap?): Uri? {
    bitmap?: return null
    val cachePath = File(cacheDir, "images")
    cachePath.mkdirs()
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val fileName = "temp_image_$timeStamp.png"
    val file = File(cachePath, fileName)
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    return androidx.core.content.FileProvider.getUriForFile(
        this,
        "${packageName}.fileprovider",
        file
    )
}

fun View.backgroundTarget() = object : CustomTarget<Drawable>() {
    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        this@backgroundTarget.background = resource
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        this@backgroundTarget.background = placeholder
    }
}
package com.core.util

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

fun openGallery(launcher: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
    }
    launcher.launch(intent)
}
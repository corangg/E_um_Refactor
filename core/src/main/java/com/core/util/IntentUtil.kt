package com.core.util

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

fun openGallery(launcher: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_PICK).apply {
        type = "image/*"
    }
    launcher.launch(intent)
}
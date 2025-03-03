package com.core.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

fun Context.isGranted(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.isDenied(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED

fun Context.isGrantedAll(vararg permissions: String) = permissions.all(::isGranted)
fun Context.isDeniedAny(vararg permissions: String) = permissions.any(::isDenied)

fun hasImagePermission() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
    Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
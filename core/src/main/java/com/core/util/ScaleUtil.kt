package com.core.util

import android.view.ViewGroup.LayoutParams

fun LayoutParams.getScaleParams(screenWith: Int, scale: Float) = apply {
    if (scale > 1) {
        width = screenWith
        height = (screenWith / scale).toInt()
    } else {
        width = (screenWith * scale).toInt()
        height = screenWith
    }
}
package com.core.util

import android.graphics.Paint

fun borderPaint(colorValue: Int, stroke: Float = 0f): Paint {
    return Paint().apply {
        color = colorValue
        style = Paint.Style.STROKE
        strokeWidth = stroke
    }
}
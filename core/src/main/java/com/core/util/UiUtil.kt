package com.core.util

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun getScreenSize(activity: Activity): Pair<Int, Int>{
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
        val metrics = activity.windowManager.currentWindowMetrics
        val bounds = metrics.bounds
        Pair(bounds.width(), bounds.height())
    }else{
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        Pair(size.x, size.y)
    }
}
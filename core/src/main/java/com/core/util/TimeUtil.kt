package com.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getLocalTimeToString(): String {
    return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
}
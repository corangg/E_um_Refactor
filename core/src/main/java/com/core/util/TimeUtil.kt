package com.core.util

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun getLocalTimeToString(): String {
    return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
}

fun subtractHHMMFromDateTime(original: String, subtractHHMM: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
    val baseTime = LocalDateTime.parse(original, formatter)

    val hour = subtractHHMM.substring(0, 2).toInt()
    val minute = subtractHHMM.substring(2, 4).toInt()

    val resultTime = baseTime.minusHours(hour.toLong()).minusMinutes(minute.toLong())

    return resultTime.format(formatter)
}

fun toHHMMFormat(hour: Int, minute: Int): String {
    return String.format(Locale.US, "%02d%02d", hour, minute)
}
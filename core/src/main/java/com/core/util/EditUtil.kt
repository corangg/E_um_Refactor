package com.core.util

import android.text.InputFilter
import android.widget.EditText

fun setEditTextMaxValue(editText: EditText, max: Int) {
    val filter = InputFilter { source, start, end, dest, dstart, dend ->
        try {
            val input = dest.toString().substring(0, dstart) +
                    source.subSequence(start, end) +
                    dest.toString().substring(dend)
            if (input.isEmpty()) return@InputFilter null

            val value = input.toInt()
            if (value <= max) null else ""
        } catch (e: NumberFormatException) {
            ""
        }
    }
    editText.filters = arrayOf(filter)
}
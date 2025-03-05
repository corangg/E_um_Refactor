package com.app.ui.custom

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.app.R

fun Context.showCustomToast(message: String) {
    val parent = (this as Activity).findViewById<ViewGroup>(android.R.id.content)
    val inflater = LayoutInflater.from(this)
    val layout = inflater.inflate(R.layout.toast_layout, parent, false)

    val textView = layout.findViewById<TextView>(R.id.text_toast)
    textView.text = message

    val toast = Toast(this)
    toast.duration = Toast.LENGTH_SHORT
    toast.setGravity(Gravity.BOTTOM, 0, 200)
    toast.view = layout
    toast.show()
}
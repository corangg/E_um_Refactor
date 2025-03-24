package com.core.ui.custom

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide

class ThreeButtonCustomDialog(
    context: Context,
    @LayoutRes private val layoutResId: Int,
    private val width: Float = 1f,
    private val height: Float = 1f
) : Dialog(context) {

    private var dialogView: View? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogView = LayoutInflater.from(context).inflate(layoutResId, null)
        setContentView(dialogView!!)

        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    override fun onStart() {
        super.onStart()
        resizeDialog(width, height)
    }

    private fun resizeDialog(widthRatio: Float, heightRatio: Float) {
        val metrics = context.resources.displayMetrics
        val width = (metrics.widthPixels * widthRatio).toInt()
        val height = (metrics.heightPixels * heightRatio).toInt()
        window?.setLayout(width, height)
    }

    fun showDialog() {
        show()
    }

    fun dismissDialog() {
        dismiss()
    }

    fun getView(): View? {
        return dialogView
    }

    fun setText(viewId: Int, text: String) {
        dialogView?.findViewById<TextView>(viewId)?.text = text
    }

    fun setButtonClickListener(viewId: Int, listener: View.OnClickListener) {
        dialogView?.findViewById<View>(viewId)?.setOnClickListener(listener)
    }

    fun setImage(viewId: Int, imageUrl: String) {
        val imageView = dialogView?.findViewById<ImageView>(viewId)
        imageView?.let {
            Glide.with(context)
                .load(imageUrl)
                .into(it)
        }
    }
}
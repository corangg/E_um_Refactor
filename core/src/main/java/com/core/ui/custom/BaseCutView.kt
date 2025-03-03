package com.core.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

abstract class BaseCutView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle), View.OnTouchListener {

    protected val transformationMatrix = Matrix()
    protected var scaleFactor = 1.0f
    protected var lastTouchX = 0f
    protected var lastTouchY = 0f
    protected var parentWidth = 0
    protected var parentHeight = 0

    init {
        setOnTouchListener(this)
        scaleType = ScaleType.MATRIX
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        parentWidth = (parent as View).width
        parentHeight = (parent as View).height
    }

    protected abstract fun judgeTouchableArea(event: MotionEvent): Boolean
    protected abstract fun drawBorder(canvas: Canvas)

    fun getParentSize(): Pair<Int, Int> {
        return Pair(parentWidth, parentHeight)
    }
}
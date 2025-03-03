package com.core.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.core.util.borderPaint

class SplitSquareView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle), View.OnTouchListener {
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var parentWidth = 0
    private var parentHeight = 0

    var areaRect = RectF()

    init {
        setOnTouchListener(this)
        scaleType = ScaleType.MATRIX
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        parentWidth = (parent as View).width
        parentHeight = (parent as View).height

        val centerX = parentWidth / 2f
        val centerY = parentHeight / 2f
        areaRect.set(centerX - 150f, centerY - 150f, centerX + 150f, centerY + 150f)
    }

    fun getParentSize(): Pair<Int, Int> {
        return Pair(parentWidth, parentHeight)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (!areaRect.contains(event.x, event.y)) {
            return false
        }

        scaleGestureDetector.onTouchEvent(event)

        if (!scaleGestureDetector.isInProgress) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.x
                    lastTouchY = event.y
                    invalidate()
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY
                    areaRect.offset(dx, dy)
                    lastTouchX = event.x
                    lastTouchY = event.y
                }
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(areaRect, borderPaint(Color.RED, 4f))
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            val centerX = areaRect.centerX()
            val centerY = areaRect.centerY()
            val newHalfWidth = areaRect.width() / 2 * scaleFactor
            val newHalfHeight = areaRect.height() / 2 * scaleFactor
            areaRect.set(
                centerX - newHalfWidth,
                centerY - newHalfHeight,
                centerX + newHalfWidth,
                centerY + newHalfHeight
            )
            invalidate()
            return true
        }
    }
}

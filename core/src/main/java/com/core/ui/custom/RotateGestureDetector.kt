package com.core.ui.custom

import android.view.MotionEvent
import kotlin.math.atan2

class RotateGestureDetector(
    private val listener: OnRotateGestureListener
) {
    interface OnRotateGestureListener {
        fun onRotate(detector: RotateGestureDetector): Boolean
    }

    private var fX: Float = 0f
    private var fY: Float = 0f
    private var sX: Float = 0f
    private var sY: Float = 0f
    var isInProgress: Boolean = false
        private set
    private var previousAngle: Float = 0f
    var rotationDegreesDelta: Float = 0f
        private set

    val focusX: Float
        get() = (fX + sX) / 2

    val focusY: Float
        get() = (fY + sY) / 2

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    fX = event.getX(0)
                    fY = event.getY(0)
                    sX = event.getX(1)
                    sY = event.getY(1)
                    previousAngle = angleBetweenLines(fX, fY, sX, sY)
                    isInProgress = true
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isInProgress && event.pointerCount == 2) {
                    val newFx = event.getX(0)
                    val newFy = event.getY(0)
                    val newSx = event.getX(1)
                    val newSy = event.getY(1)
                    val newAngle = angleBetweenLines(newFx, newFy, newSx, newSy)
                    rotationDegreesDelta = newAngle - previousAngle
                    previousAngle = newAngle

                    listener.onRotate(this)
                }
            }

            MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_UP -> {
                isInProgress = false
            }
        }
        return true
    }

    private fun angleBetweenLines(fx: Float, fy: Float, sx: Float, sy: Float): Float {
        return Math.toDegrees(atan2((sy - fy).toDouble(), (sx - fx).toDouble())).toFloat()
    }
}
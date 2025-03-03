package com.core.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

class EditableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle), View.OnTouchListener {
    private val matrix = Matrix()
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private val rotateGestureDetector = RotateGestureDetector(RotateListener())

    var viewId: Int = -1
    var onMatrixChangeCallback: ((Matrix, Float, Float, Int) -> Unit)? = null
    var onSelectCallback: ((Int) -> Unit)? = null

    private var bolder = Paint()

    var scaleFactor = 1.0f
    var rotationDegrees = 0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    var saturationValue = 1f
    var brightnessValue = 1f
    var transparencyValue = 1f

    var isSelectedValue = false

    init {
        setOnTouchListener(this)
        scaleType = ScaleType.MATRIX
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val transPos = getTransformedPoints()
        if (!judgeTouchableArea(event.x, event.y, transPos)) {
            return false
        }

        scaleGestureDetector.onTouchEvent(event)
        rotateGestureDetector.onTouchEvent(event)

        if (!scaleGestureDetector.isInProgress && !rotateGestureDetector.isInProgress) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    onSelectCallback?.invoke(viewId)
                    lastTouchX = event.x
                    lastTouchY = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY

                    matrix.postTranslate(dx, dy)
                    imageMatrix = matrix

                    lastTouchX = event.x
                    lastTouchY = event.y

                }

                MotionEvent.ACTION_UP -> {
                    onMatrixChangeCallback?.invoke(matrix, scaleFactor, rotationDegrees, viewId)
                }
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)
    }

    private fun judgeTouchableArea(x: Float, y: Float, polygon: FloatArray): Boolean {
        var intersectCount = 0
        for (i in polygon.indices step 2) {
            val x1 = polygon[i]
            val y1 = polygon[i + 1]
            val x2 = polygon[(i + 2) % polygon.size]
            val y2 = polygon[(i + 3) % polygon.size]

            if (((y1 > y) != (y2 > y)) &&
                (x < (x2 - x1) * (y - y1) / (y2 - y1) + x1)
            ) {
                intersectCount++
            }
        }
        return (intersectCount % 2) == 1
    }

    private fun getTransformedPoints(): FloatArray {
        val drawable = drawable ?: return floatArrayOf()
        val width = drawable.intrinsicWidth.toFloat()
        val height = drawable.intrinsicHeight.toFloat()
        val points = floatArrayOf(
            0f, 0f,
            width, 0f,
            width, height,
            0f, height
        )
        matrix.mapPoints(points)
        return points
    }

    private fun drawBorder(canvas: Canvas) {
        val points = getTransformedPoints()
        if (points.isEmpty()) return
        val path = Path().apply {
            moveTo(points[0], points[1])
            lineTo(points[2], points[3])
            lineTo(points[4], points[5])
            lineTo(points[6], points[7])
            close()
        }
        if (isSelectedValue) {
            bolder = Paint().apply {
                color = Color.WHITE
                style = Paint.Style.STROKE
                strokeWidth = 4f

            }
            canvas.drawPath(path, bolder)
        } else {
            bolder = Paint().apply {
                color = Color.TRANSPARENT
                style = Paint.Style.STROKE
                strokeWidth = 4f

            }
            canvas.drawPath(path, bolder)
        }
    }

    fun setImageTransparency(alpha: Float) {
        val clampedAlpha = Math.max(0f, Math.min(alpha, 100f)) / 100f
        transparencyValue = alpha
        this.alpha = clampedAlpha
    }

    fun setImageSaturation(saturation: Float) {
        saturationValue = (saturation) / 100f
        colorFilter = applyColorFilter(saturationValue, brightnessValue)
    }

    fun setImageBrightness(brightness: Float) {
        brightnessValue = 0.008f * (brightness - 100f) * 2 + 1f
        colorFilter = applyColorFilter(saturationValue, brightnessValue)
    }

    private fun applyColorFilter(saturationValue: Float, brightnessValue: Float): ColorFilter {
        val colorMatrix = ColorMatrix()

        val saturationMatrix = ColorMatrix()
        saturationMatrix.setSaturation(saturationValue)

        val brightnessMatrix = ColorMatrix()
        brightnessMatrix.setScale(brightnessValue, brightnessValue, brightnessValue, 1f)

        colorMatrix.postConcat(saturationMatrix)
        colorMatrix.postConcat(brightnessMatrix)

        return ColorMatrixColorFilter(colorMatrix)
    }

    fun setMatrixData(matrixValue: Array<Float>, scale: Float, degrees: Float) {
        if (matrixValue.size == 9) {
            val floatArray = matrixValue.toFloatArray()
            matrix.setValues(floatArray)
            imageMatrix = matrix

            scaleFactor = scale
            rotationDegrees = degrees
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scale = detector.scaleFactor
            scaleFactor *= scale
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f))

            matrix.postScale(scale, scale, detector.focusX, detector.focusY)
            imageMatrix = matrix
            invalidate()
            return true
        }
    }

    private inner class RotateListener : RotateGestureDetector.OnRotateGestureListener {
        override fun onRotate(detector: RotateGestureDetector): Boolean {
            val rotation = detector.rotationDegreesDelta
            rotationDegrees += rotation
            matrix.postRotate(rotation, detector.focusX, detector.focusY)
            imageMatrix = matrix
            invalidate()
            return true
        }
    }
}
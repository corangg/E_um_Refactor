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
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import kotlin.math.abs

class TextImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatEditText(context, attrs, defStyle), View.OnTouchListener {
    private val matrix = Matrix()
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private val rotateGestureDetector = RotateGestureDetector(RotateListener())

    var viewId: Int = -1

    var onSelectCallback: ((Int) -> Unit)? = null
    var onTextChangeCallback: ((String, Matrix, Float, Float, Int) -> Unit)? = null

    var scaleFactor = 1.0f
    var rotationDegrees = 0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    var saturationValue = 1f
    var brightnessValue = 1f
    var transparencyValue = 1f

    var isSelectedValue = false
    private var touchListener = true

    private var bolder = Paint()
    private var isEditable = false

    var fillBackgroundPaint = Paint().apply {
        color = Color.TRANSPARENT
        style = Paint.Style.FILL
    }

    init {
        setOnTouchListener(this)
        ViewCompat.setTranslationZ(this, 1f)
        gravity = Gravity.CENTER
        isFocusable = true
        isFocusableInTouchMode = true
        isClickable = true
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        super.dispatchTouchEvent(event)
        if (!touchListener) {
            (parent as? ViewGroup)?.onTouchEvent(event)
        }
        return touchListener
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        touchListener = true
        val transPos = getTransformedPoints()
        if (!judgeTouchableArea(event.x, event.y, transPos)) {
            isEditable = false
            clearFocus()
            hideKeyboard()
            onTextChangeCallback?.invoke(
                text.toString(),
                matrix,
                scaleFactor,
                rotationDegrees,
                viewId
            )
            touchListener = false
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
                    isEditable = true

                    val transformedPoint = getInverseTransformedPoint(event.x, event.y)
                    val offset = getOffsetForPosition(transformedPoint[0], transformedPoint[1])
                    setSelection(offset)
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY

                    if (abs(dx) > 1 || abs(dy) > 1) {
                        isEditable = false
                    }
                    matrix.postTranslate(dx, dy)
                    lastTouchX = event.x
                    lastTouchY = event.y
                }

                MotionEvent.ACTION_UP -> {
                    if (isEditable) {
                        showKeyboard()
                    }
                }
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        drawBorder(canvas)
        canvas.save()
        canvas.concat(matrix)
        super.onDraw(canvas)
        canvas.restore()
    }

    private fun getInverseTransformedPoint(x: Float, y: Float): FloatArray {
        val inverseMatrix = Matrix()
        if (matrix.invert(inverseMatrix)) {
            val pts = floatArrayOf(x, y)
            inverseMatrix.mapPoints(pts)
            return pts
        }
        return floatArrayOf(x, y)
    }

    private fun drawBorder(canvas: Canvas) {
        val points = getTransformedPoints()
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
            canvas.drawPath(path, fillBackgroundPaint)
        } else {
            bolder = Paint().apply {
                color = Color.TRANSPARENT
                style = Paint.Style.STROKE
                strokeWidth = 4f

            }
            canvas.drawPath(path, bolder)
            canvas.drawPath(path, fillBackgroundPaint)
        }
    }

    private fun getTransformedPoints(): FloatArray {
        val width = this.width.toFloat()
        val height = this.height.toFloat()
        val points = floatArrayOf(
            0f, 0f,
            width, 0f,
            width, height,
            0f, height
        )
        matrix.mapPoints(points)
        return points
    }

    private fun judgeTextTouchableArea(x: Float, y: Float): Boolean {
        if (text.isNullOrEmpty()) return false
        val transformedPoint = getInverseTransformedPoint(x, y)
        val offset = getOffsetForPosition(transformedPoint[0], transformedPoint[1])
        return offset in 0 until text!!.length
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

    private fun showKeyboard() {
        setFocusableInTouchMode(true)
        requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        setFocusableInTouchMode(false)
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun getMatrixValues(): FloatArray {
        val matrixValues = FloatArray(9)
        matrix.getValues(matrixValues)
        return matrixValues
    }

    fun setMatrixData(matrixValue: Array<Float>, scale: Float, degrees: Float) {
        if (matrixValue.size == 9) {
            val floatArray = matrixValue.toFloatArray()
            matrix.setValues(floatArray)

            scaleFactor = scale
            rotationDegrees = degrees
        }
    }

    fun setTextTransparency(alpha: Float) {
        val clampedAlpha = Math.max(0f, Math.min(alpha, 100f)) / 100f
        transparencyValue = alpha
        this.alpha = clampedAlpha
    }

    fun setTextSaturation(saturation: Float) {
        saturationValue = (saturation) / 100f
        paint.colorFilter = applyColorFilter(saturationValue, brightnessValue)
    }

    fun setTextBrightness(brightness: Float) {
        brightnessValue = 0.008f * (brightness - 100f) * 2 + 1f
        paint.colorFilter = applyColorFilter(saturationValue, brightnessValue)
    }

    fun setFillBackgroundColor(color: Int) {
        fillBackgroundPaint.color = color
        invalidate()
    }

    fun setFont(font: Typeface?) {
        font ?: return
        this.typeface = font
    }

    fun getTextColor(): Int {
        return currentTextColor
    }

    fun getTextTypeface(): Typeface {
        return typeface
    }

    fun getTextContent(): String {
        return text.toString()
    }

    fun getTextSizeValue(): Int {
        return (textSize / 3).toInt()
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

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scale = detector.scaleFactor
            scaleFactor *= scale
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f))

            matrix.postScale(scale, scale, detector.focusX, detector.focusY)
            invalidate()
            isEditable = false
            return true
        }
    }

    private inner class RotateListener : RotateGestureDetector.OnRotateGestureListener {
        override fun onRotate(detector: RotateGestureDetector): Boolean {
            val rotation = detector.rotationDegreesDelta
            rotationDegrees += rotation
            matrix.postRotate(rotation, detector.focusX, detector.focusY)
            invalidate()
            isEditable = false
            return true
        }
    }
}
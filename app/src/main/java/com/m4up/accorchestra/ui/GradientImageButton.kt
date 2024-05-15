package com.m4up.accorchestra.ui

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.core.content.res.getStringOrThrow
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.PathParser
import androidx.core.graphics.scaleMatrix
import com.m4up.accorchestra.R

class GradientImageButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var colors: IntArray? = null
    private var viewportWidth: Float = 1f
    private var viewportHeight: Float = 1f
    private var pathData: String = ""
        set(value) {
            field = value
            setPathDataFromString(value)
            rescalePathForRealViewSize(width, height)
        }

    private val paint = Paint().apply {
        style = Paint.Style.FILL
    }
    private var iconPath = Path()
    private val scalingMatrix = Matrix()

    init {
        Log.d(GradientImageButton.GRADIENT_IMAGE_BUTTON_TAG,"Enter GradientImageButton init")
        attrs?.let { parseAttrs(it) }
        updateGradient(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateGradient(w, h)
        rescalePathForRealViewSize(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(iconPath, paint)
    }

    private fun parseAttrs(attrs: AttributeSet) {
        context.withStyledAttributes(attrs, R.styleable.GradientImageButton) {
            val startColor = getColor(R.styleable.GradientImageButton_startColor, DEFAULT_COLOR)
            val centerColor = getColor(R.styleable.GradientImageButton_centerColor, DEFAULT_COLOR)
            val finishColor = getColor(R.styleable.GradientImageButton_finishColor, DEFAULT_COLOR)
            colors = intArrayOf(startColor, centerColor, finishColor)

            viewportWidth = getFloat(R.styleable.GradientImageButton_viewportWidth, 1f)
            viewportHeight = getFloat(R.styleable.GradientImageButton_viewportHeight, 1f)
            pathData = getString(R.styleable.GradientImageButton_pathData) ?: ""
        }
    }

    private fun setPathDataFromString(str: String) {
        iconPath = PathParser.createPathFromPathData(str)
    }

    private fun rescalePathForRealViewSize(w: Int, h: Int) {
        if (width > 0 && height > 0) {
            val scaleX = w.toFloat() / viewportWidth
            val scaleY = h.toFloat() / viewportHeight
            scalingMatrix.setScale(scaleX, scaleY, 0f, 0f)
            iconPath.transform(scalingMatrix)
        }
    }

    private fun updateGradient(w: Int, h: Int) {
        val currColors = colors
        if (currColors != null) {
            val gradient = LinearGradient(
                w * 3f / 10f,
                h * 3f / 10f,
                w.toFloat() * 7f / 10f,
                h.toFloat() * 7f / 10f,
                currColors,
                null,
                Shader.TileMode.CLAMP
            )
            paint.shader = gradient
        }
    }

    companion object {
        const val GRADIENT_IMAGE_BUTTON_TAG = "gradient_image_button"
        const val DEFAULT_COLOR: Int = 0x00AAAAAA
    }
}
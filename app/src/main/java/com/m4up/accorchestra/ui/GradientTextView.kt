package com.m4up.accorchestra.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable.ShaderFactory
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.withStyledAttributes
import com.m4up.accorchestra.R

class GradientTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var colors: IntArray? = null
    private val gradientPositions = floatArrayOf(0.2f, 1f, 2f)
    private val boundsBuffer = Rect()

     init {
        Log.d(GRADIENT_TEXT_VIEW_TAG,"Enter GradientTextView init")
         attrs?.let { parseAttrs(it) }
         setGradient(text, 0, text.length)
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        setGradient(text, start, start + lengthAfter)
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    private fun parseAttrs(attrs: AttributeSet) {
        context.withStyledAttributes(attrs, R.styleable.GradientTextView) {
            val startColor = getColor(R.styleable.GradientTextView_textStartColor, currentTextColor)
            val centerColor = getColor(R.styleable.GradientTextView_textCenterColor, currentTextColor)
            val finishColor = getColor(R.styleable.GradientTextView_textFinishColor, currentTextColor)
            colors = intArrayOf(startColor, centerColor, finishColor)
        }
    }

    private fun setGradient(givenText: CharSequence?, start: Int, finish: Int) {
        val currColors = colors ?: return
        val currText = givenText ?: return
        setTextColor(currColors[0])
        if (currColors[0] != currColors[1] || currColors[1] != currColors[2]) {
            paint.getTextBounds(currText, start, finish, boundsBuffer)
            val shader = LinearGradient(
                boundsBuffer.left.toFloat(),
                boundsBuffer.bottom.toFloat(),
                boundsBuffer.right.toFloat(),
                boundsBuffer.top.toFloat(),
                currColors,
                null,//gradientPositions,
                Shader.TileMode.REPEAT
            )
            paint.shader = shader
        }
    }

    companion object {
        private const val GRADIENT_TEXT_VIEW_TAG = "gradient_textview"
    }
}
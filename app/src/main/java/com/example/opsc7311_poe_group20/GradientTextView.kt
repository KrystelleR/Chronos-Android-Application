package com.example.opsc7311_poe_group20

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView


class GradientTextView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()
        val width = paint.measureText(text.toString())

        val shader = LinearGradient(
            0f, 0f, width, textSize,
            intArrayOf(
                Color.parseColor("#AE8625"),
                Color.parseColor("#F7EF8A"),
                Color.parseColor("#D2AC47"),
                Color.parseColor("#EDC967")
            ),
            null,
            Shader.TileMode.CLAMP
        )
        paint.shader = shader

        super.onDraw(canvas)
    }
}

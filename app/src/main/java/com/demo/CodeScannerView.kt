package com.demo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CodeScannerView(
    context: Context,
    attribute: AttributeSet,
) : View(context, attribute) {
    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scannerSize = 600
        val centerX = width / 2
        val centerY = height / 2
        val top = centerY - scannerSize / 2
        val left = centerX - scannerSize / 2
        val right = centerX + scannerSize / 2
        val bottom = centerY + scannerSize / 2

        val cornerLength = 40
        val cornerRadius = 20f
        paint.color = Color.parseColor("#80000000")
        paint.style = Paint.Style.FILL

        canvas.drawRect(0f, 0f, width.toFloat(), top.toFloat(), paint)
        canvas.drawRect(0f, top.toFloat(), left.toFloat(), bottom.toFloat(), paint)
        canvas.drawRect(right.toFloat(), top.toFloat(), width.toFloat(), bottom.toFloat(), paint)
        canvas.drawRect(0f, bottom.toFloat(), width.toFloat(), height.toFloat(), paint)

        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.strokeCap = Paint.Cap.ROUND
        // Góc trên bên trái
        canvas.drawLine(left.toFloat(), top.toFloat(), (left + cornerLength).toFloat(), top.toFloat(), paint)
        canvas.drawLine(left.toFloat(), top.toFloat(), left.toFloat(), (top + cornerLength).toFloat(), paint)

        // Góc trên bên phải
        canvas.drawLine(right.toFloat(), top.toFloat(), (right - cornerLength).toFloat(), top.toFloat(), paint)
        canvas.drawLine(right.toFloat(), top.toFloat(), right.toFloat(), (top + cornerLength).toFloat(), paint)

        // Góc dưới bên trái
        canvas.drawLine(left.toFloat(), bottom.toFloat(), (left + cornerLength).toFloat(), bottom.toFloat(), paint)
        canvas.drawLine(left.toFloat(), bottom.toFloat(), left.toFloat(), (bottom - cornerLength).toFloat(), paint)

        // Góc dưới bên phải
        canvas.drawLine(right.toFloat(), bottom.toFloat(), (right - cornerLength).toFloat(), bottom.toFloat(), paint)
        canvas.drawLine(right.toFloat(), bottom.toFloat(), right.toFloat(), (bottom - cornerLength).toFloat(), paint)
    }
}

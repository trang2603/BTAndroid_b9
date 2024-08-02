package com.demo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CodeScannerView(
    context: Context,
    attribute: AttributeSet,
) : View(context, attribute) {
    private val paint = Paint()
    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scannerSizePercentage = 0.5 // 50% của chiều rộng hoặc chiều cao màn hình
        val minSize = Math.min(width, height)
        val scannerSize = (minSize * scannerSizePercentage).toInt()
        val centerX = width / 2
        val centerYOffsetPercentage = 0.1 // 10% của chiều cao màn hình
        val centerYOffset = (height * centerYOffsetPercentage).toInt()
        val centerY = (height / 2) - centerYOffset
        val top = centerY - scannerSize / 2
        val left = centerX - scannerSize / 2
        val right = centerX + scannerSize / 2
        val bottom = centerY + scannerSize / 2
        val cornerRadius = 60f // Bán kính của góc bo
        val padding = 10f // Khoảng cách từ hình chữ nhật nhỏ hơn đến đường line bo góc

        // Thiết lập paint cho phần mờ bên ngoài
        paint.color = Color.parseColor("#80000000")
        paint.style = Paint.Style.FILL

        // Sử dụng Xfermode để vẽ phần trong suốt với các góc bo tròn
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val tempCanvas = Canvas(bitmap)

        // Vẽ hình chữ nhật lớn với góc bo tròn để che toàn bộ màn hình
        tempCanvas.drawRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), cornerRadius, cornerRadius, paint)

        // Vẽ hình chữ nhật nhỏ hơn với góc bo tròn để tạo vùng trong suốt
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        tempCanvas.drawRoundRect(
            RectF(left + padding, top + padding, right - padding, bottom - padding),
            cornerRadius,
            cornerRadius,
            paint,
        )
        paint.xfermode = null

        // Vẽ bitmap lên canvas chính
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        // Thiết lập paint cho các đường line bo góc
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.strokeCap = Paint.Cap.ROUND

        val cornerLength = 150f // Độ dài của đường line ở góc

        // Vẽ các góc trên bên trái với bo góc
        canvas.drawLine(left + cornerRadius, top.toFloat(), left + cornerLength, top.toFloat(), paint)
        canvas.drawLine(left.toFloat(), top + cornerRadius, left.toFloat(), top + cornerLength, paint)

        // Vẽ các góc trên bên phải với bo góc
        canvas.drawLine(right - cornerLength, top.toFloat(), right - cornerRadius, top.toFloat(), paint)
        canvas.drawLine(right.toFloat(), top + cornerRadius, right.toFloat(), top + cornerLength, paint)

        // Vẽ các góc dưới bên trái với bo góc
        canvas.drawLine(left + cornerRadius, bottom.toFloat(), left + cornerLength, bottom.toFloat(), paint)
        canvas.drawLine(left.toFloat(), bottom - cornerLength, left.toFloat(), bottom - cornerRadius, paint)

        // Vẽ các góc dưới bên phải với bo góc
        canvas.drawLine(right - cornerLength, bottom.toFloat(), right - cornerRadius, bottom.toFloat(), paint)
        canvas.drawLine(right.toFloat(), bottom - cornerLength, right.toFloat(), bottom - cornerRadius, paint)

        // Vẽ các đoạn bo góc
        canvas.drawArc(RectF(left.toFloat(), top.toFloat(), left + 2 * cornerRadius, top + 2 * cornerRadius), 180f, 90f, false, paint)
        canvas.drawArc(RectF(right - 2 * cornerRadius, top.toFloat(), right.toFloat(), top + 2 * cornerRadius), 270f, 90f, false, paint)
        canvas.drawArc(RectF(left.toFloat(), bottom - 2 * cornerRadius, left + 2 * cornerRadius, bottom.toFloat()), 90f, 90f, false, paint)
        canvas.drawArc(RectF(right - 2 * cornerRadius, bottom - 2 * cornerRadius, right.toFloat(), bottom.toFloat()), 0f, 90f, false, paint)
    }
}

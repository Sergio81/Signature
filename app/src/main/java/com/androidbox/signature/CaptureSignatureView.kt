package com.androidbox.signature

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.util.AttributeSet
import android.view.View
import java.io.ByteArrayOutputStream
import kotlin.math.abs


class CaptureSignatureView(context: Context, attr: AttributeSet) : View(context, attr) {
    private var globalBitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private val path: Path = Path()
    private val bitmapPaint: Paint = Paint(Paint.DITHER_FLAG)
    private val paint: Paint = Paint()
    private var mX: Float = 0.toFloat()
    private var mY: Float = 0.toFloat()
    private val touchTolerance = 4f
    private val lineThickness = 4f

    val bytes: ByteArray
        get() {
            val b = bitmap
            val outputStream = ByteArrayOutputStream()

            b.compress(
                Bitmap.CompressFormat.PNG,      // format
                100,                    // quality
                outputStream                    // stream
            )

            return outputStream.toByteArray()
        }

    val bitmap: Bitmap
        get() {
            val view = this.parent as View
            val tmpBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val tmpCanvas = Canvas(tmpBitmap)

            view.layout(view.left, view.top, view.right, view.bottom)
            view.draw(tmpCanvas)

            return tmpBitmap
        }

    var containSignature: Boolean = false
        private set

    init {
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = Color.argb(255, 0, 0, 0)
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = lineThickness
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        globalBitmap =
            Bitmap.createBitmap(
                w,                                                  // width
                if (h > 0) h else (this.parent as View).height,     // height
                Bitmap.Config.ARGB_8888                             // config
            )

        canvas = Canvas(globalBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(globalBitmap!!, 0f, 0f, bitmapPaint)
        canvas.drawPath(path, paint)
    }

    private fun touchStart(x: Float, y: Float) {
        path.reset()
        path.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = abs(x - mX)
        val dy = abs(y - mY)

        if (dx >= touchTolerance || dy >= touchTolerance) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touchUp() {
        if (!path.isEmpty) {
            path.lineTo(mX, mY)
            canvas!!.drawPath(path, paint)
        } else {
            canvas!!.drawPoint(mX, mY, paint)
        }

        containSignature = true
        path.reset()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        super.onTouchEvent(e)
        val x = e.x
        val y = e.y

        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }

        parent.requestDisallowInterceptTouchEvent(true)

        return true
    }

    fun clearCanvas() {
        containSignature = false
        canvas!!.drawColor(Color.WHITE)
        invalidate()
    }
}
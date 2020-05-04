package com.example.kidsdrawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var _drawPath: CustomPath? = null
    private var _canvasBitmap: Bitmap? = null
    private var _drawPaint: Paint? = null
    private var _canvasPaint: Paint? = null
    private var _brushSize: Float = 0.toFloat()
    private var _color = Color.BLACK
    private var _canvas: Canvas? = null
    private val _paths = ArrayList<CustomPath>()

    init {
        setUpDrawing()
    }

    private fun setUpDrawing() {
        _drawPaint = Paint()
        _drawPath = CustomPath(_color, _brushSize)
        _drawPath!!.color = _color
        _drawPaint!!.style = Paint.Style.STROKE
        _drawPaint!!.strokeJoin = Paint.Join.ROUND
        _drawPaint!!.strokeCap = Paint.Cap.ROUND
        _canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    fun setBrushSize(newSize: Float) {
        _brushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize,
                resources.displayMetrics)
        _drawPaint!!.strokeWidth = _brushSize
    }

    fun setColor(colorName: String) {
        val color = Color.parseColor(colorName)
        _color = color
        _drawPaint!!.color = color
    }

    fun onUndo() {
        if(_paths.isNotEmpty()) {
            _paths.removeAt(_paths.size - 1)
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        _canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        _canvas = Canvas(_canvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(_canvasBitmap!!, 0f, 0f, _canvasPaint)

        for(path in _paths) {
            _drawPaint!!.strokeWidth = path.brushThickness
            _drawPaint!!.color = path.color
            canvas.drawPath(path, _drawPaint!!)
        }

        if(!_drawPath!!.isEmpty) {
            _drawPaint!!.strokeWidth = _drawPath!!.brushThickness
            _drawPaint!!.color = _drawPath!!.color
            canvas.drawPath(_drawPath!!, _drawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                _drawPath!!.color = _color
                _drawPath!!.brushThickness = _brushSize

                _drawPath!!.reset()

                if (touchX != null && touchY != null)
                    _drawPath!!.moveTo(touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchX != null && touchY != null)
                    _drawPath!!.lineTo(touchX, touchY)
            }
            MotionEvent.ACTION_UP -> {
                _paths.add(_drawPath!!)
                _drawPath = CustomPath(_color, _brushSize)
            }
            else -> return false
        }
        invalidate()

        return true
    }

    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path() {

    }
}
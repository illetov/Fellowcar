package com.fellowcar.android.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.fellowcar.android.R

//DO NOT CHANGE!!!!
/*THIS CLASS WILL BE BUILD IN THE FUTURE
* CAPABILITIES:
* -VIEW FOR PRESET DIRECTION (TWO POINTS)
* -VIEW CONSTRAINT TEXTVIEW FOR ADD F.L S.L
* */


class DirectionLineView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paintCircleStart: Paint
    private val paintCircleFinish: Paint
    private val paintLine: Paint
    private val effects: PathEffect

    init {
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DirectionLineView)
//        val isNearby = typedArray.getBoolean(R.styleable.DirectionLineView_isNearby, false)
        typedArray.recycle()

        paintCircleStart = Paint()
        paintCircleFinish = Paint()
        paintLine = Paint()
        effects = DashPathEffect(floatArrayOf(10f, 10f), 0f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val x = measuredWidth / 2
        val y = measuredHeight

        val radius = 15


        paintLine.color = Color.parseColor("#8a8a8a")
        paintLine.strokeWidth = 4f
        paintLine.isAntiAlias = true
        paintLine.pathEffect = effects
        paintLine.strokeCap = Paint.Cap.ROUND
        paintLine.strokeJoin = Paint.Join.ROUND

        paintCircleStart.style = Paint.Style.FILL
        paintCircleStart.isAntiAlias = true
        paintCircleStart.color = Color.parseColor("#4cda64")

        paintCircleFinish.style = Paint.Style.FILL
        paintCircleFinish.isAntiAlias = true
        paintCircleFinish.color = Color.parseColor("#ff3b2f")


        canvas.drawCircle(x/2.toFloat(), 0f, radius.toFloat(), paintCircleStart)
//        canvas.drawLine(x.toFloat(), y/2.toFloat(), x.toFloat(), y / 4.toFloat(), paintLine)
        canvas.drawCircle(x.toFloat(), y.toFloat(), radius.toFloat(), paintCircleFinish)

        /*some stuff isn't work */
        //  canvas.drawCircle(x.toFloat(), y.toFloat(), radius.toFloat(), paintCircleStart)
        //  canvas.drawLine(x.toFloat(), y.toFloat(), x.toFloat(), y / 2.toFloat(), paintLine)
        //  canvas.drawCircle(x.toFloat(), (y / 2).toFloat(), radius.toFloat(), paintCircleFinish)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val dia = 15 * 2
        val w = View.resolveSize(dia, widthMeasureSpec)
        val h = View.resolveSize(dia, heightMeasureSpec)
        setMeasuredDimension(w, h)
    }

}

package com.ztn.commom.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ztn.commom.R
import com.ztn.common.utils.dip2px
import com.ztn.common.utils.sp2px
import kotlin.collections.ArrayList

/**
 * Created by 冒险者ztn on 2019-06-07.
 * 涂鸦界面
 */
class DoodlingView : View {

    companion object {
        /**
         * 绘制的三种状态
         */
        const val NOT_ON_DRAW = 0
        const val DRAW_LINE = 1
        const val DRAW_TEXT = 2
    }

    private var drawState = NOT_ON_DRAW

    //查看下方 data class 即可
    private lateinit var mPairList: ArrayList<Any>
    //绘制线条宽度
    private var lineStrokeWidth: Float = 0.0f
    //绘制文字宽度
    private var textStrokeWidth: Float = 0.0f

    @ColorRes
    var drawColor: Int = 0

    private var bitmap: Bitmap? = null

    private var doodlingPathNamListener: DoodlingPathNamListener? = null
    private var doodlingStateListener: DoodlingOnActionDownStateListener? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initData()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initData()
    }

    private fun initData() {
        lineStrokeWidth = 10f.dip2px
        textStrokeWidth = 1f.dip2px
        drawColor = R.color.color_35FF0000
        mPairList = ArrayList()
    }

    fun addPathNumChangeListener(doodlingPathNamListener: DoodlingPathNamListener) {
        this.doodlingPathNamListener = doodlingPathNamListener
    }

    fun addDrawStateChangeListener(doodlingStateListener: DoodlingOnActionDownStateListener) {
        this.doodlingStateListener = doodlingStateListener
    }

    /**
     * 设置绘制的状态
     */
    fun setDrawState(state: Int) {
        if (this.drawState == state) {
            this.drawState = NOT_ON_DRAW
        } else {
            this.drawState = state
        }
        doodlingStateListener?.stateOnActionDown(drawState)

    }

    /**
     * 获取当前绘制的状态
     */
    fun getDrawState(): Int {
        return drawState
    }

    /**
     * 设置背景图片
     */
    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }

    private fun getLastData(): Any? {
        return if (mPairList.isNotEmpty()) {
            mPairList[mPairList.lastIndex]
        } else {
            null
        }
    }

    /**
     * 删除上一次绘制的path,返回当前的剩余图层
     */
    fun delPath(): Int {
        if (mPairList.isNotEmpty()) {
            mPairList.remove(getLastData())
        }
        doodlingPathNamListener?.pathNumChange(mPairList.size)
        invalidate()
        return mPairList.size
    }

    /**
     * 新建一条 path
     */
    private fun startDrawLine(x: Float, y: Float) {
        val paint = Paint()
        paint.color = ContextCompat.getColor(context, drawColor)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = lineStrokeWidth
        mPairList.add(DrawPaintData(Path(), paint))
        doodlingPathNamListener?.pathNumChange(mPairList.size)
        (getLastData() as DrawPaintData).path.moveTo(x, y)
        invalidate()
    }

    /**
     * 在新建的 path 上画图
     */
    private fun drawPath(x: Float, y: Float) {
        (getLastData() as DrawPaintData).path.lineTo(x, y)
        invalidate()
    }

    /**
     * 在特定的坐标处画文字
     */
    private fun startDrawText(x: Float, y: Float) {
        val paint = Paint()
        paint.color = ContextCompat.getColor(context, drawColor)
        paint.style = Paint.Style.FILL
        paint.strokeWidth = textStrokeWidth
        paint.textSize = 20f.sp2px
        paint.isAntiAlias = true
        mPairList.add(DrawTextData("", x, y, paint))
        doodlingPathNamListener?.pathNumChange(mPairList.size)
    }

    fun drawText(text: String) {
        if (getLastData() is DrawTextData) {
            (getLastData() as DrawTextData).text = text
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        bitmap?.apply {
            canvas?.drawBitmap(this, 0f, 0f, null)
        }

        for (i in 0 until mPairList.size) {
            val drawData = mPairList[i]
            if (drawData is DrawPaintData) {
                canvas?.drawPath(
                    drawData.path,
                    drawData.paint
                )
            } else if (drawData is DrawTextData) {
                canvas?.drawText(
                    drawData.text,
                    drawData.x,
                    drawData.y,
                    drawData.paint
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    doodlingStateListener?.stateOnActionDown(drawState)

                    return when (drawState) {
                        DRAW_LINE -> {
                            startDrawLine(x, y)
                            true

                        }
                        DRAW_TEXT -> {
                            startDrawText(x, y)
                            true

                        }
                        else -> {
                            false
                        }
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    return if (drawState == DRAW_LINE) {
                        drawPath(x, y)
                        true
                    } else {
                        false
                    }
                }
                else -> {
                    return false
                }
            }
        }
        return false
    }

    interface DoodlingPathNamListener {
        fun pathNumChange(num: Int)
    }

    interface DoodlingOnActionDownStateListener {
        fun stateOnActionDown(state: Int)
    }

    fun destroy() {
        //重置所有属性
        drawColor = R.color.color_35FF0000
        lineStrokeWidth = 10f.dip2px
        textStrokeWidth = 1f.dip2px

        //清空
        mPairList.clear()
    }

}

/**
 * 绘制的线的数据
 */
data class DrawPaintData(val path: Path, val paint: Paint)

/**
 * 绘制的文字的数据
 */
data class DrawTextData(var text: String, val x: Float, val y: Float, val paint: Paint)



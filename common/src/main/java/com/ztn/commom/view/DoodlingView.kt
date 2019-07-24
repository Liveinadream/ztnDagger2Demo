package com.ztn.commom.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.ztn.commom.R
import com.ztn.common.utils.dip2px
import com.ztn.common.utils.sp2px
import java.util.*

/**
 * Created by 冒险者ztn on 2019-06-07.
 * 涂鸦界面
 */
class DoodlingView : View {

    private val mPairList = ArrayList<Any>() //查看下方 data class 即可
    var lineStrokeWidth = 10f.dip2px
    var textStrokeWidth = 1f.dip2px

    @ColorRes
    private var drawColor: Int = R.color.color_35FF0000

    private var bitmap: Bitmap? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 设置背景图片
     */
    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    /**
     * 设置绘制颜色
     */
    fun setDrawColor(@ColorRes color: Int) {
        drawColor = color
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
        return mPairList.size
    }

    /**
     * 新建一条 path
     */
    fun startDrawLine(x: Float, y: Float) {
        val paint = Paint()
        paint.color = ContextCompat.getColor(context, drawColor)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = lineStrokeWidth
        mPairList.add(DrawPaintData(Path(), paint))
        (getLastData() as DrawPaintData).path.moveTo(x, y)
        invalidate()
    }

    /**
     * 在新建的 path 上画图
     */
    fun drawPath(x: Float, y: Float) {
        (getLastData() as DrawPaintData).path.lineTo(x, y)
        invalidate()
    }

    /**
     * 在特定的坐标处画文字
     */
    fun statDrawText(x: Float, y: Float) {
        val paint = Paint()
        paint.color = ContextCompat.getColor(context, drawColor)
        paint.style = Paint.Style.FILL
        paint.strokeWidth = textStrokeWidth
        paint.textSize = 20f.sp2px
        paint.isAntiAlias = true

        mPairList.add(DrawTextData("", x, y, paint))
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
            if (mPairList[i] is DrawPaintData) {
                canvas?.drawPath(
                    (mPairList[i] as DrawPaintData).path,
                    (mPairList[i] as DrawPaintData).paint
                )
            } else if (mPairList[i] is DrawTextData) {
                canvas?.drawText(
                    (mPairList[i] as DrawTextData).text,
                    (mPairList[i] as DrawTextData).x,
                    (mPairList[i] as DrawTextData).y,
                    (mPairList[i] as DrawTextData).paint
                )
            }
        }
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


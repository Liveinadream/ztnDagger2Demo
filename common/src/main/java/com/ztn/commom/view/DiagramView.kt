package com.ztn.commom.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.annotation.ColorInt
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import com.ztn.commom.R

/**
 * Created by 冒险者ztn on 2019/3/14.
 * 曲线图
 */
class DiagramView : View {


    private var paint: Paint = Paint()
    private var pathList = ArrayList<Path>()
    private var showHeight = 0f         //波浪中间高度所在位置
    private var screenWidth = 0f        //绘制宽度
    private var screenHeight = 0f       //绘制高度
    private var xOffset = 0f            //随时间偏移量
    private var mWaveHeight = 100f      //浪的高度
    private var speed = 2000f
    private var mWaveWidth = 200f
    private var mWaveCount = 0

    private var animator = ValueAnimator()


    constructor(context: Context) : super(context) {
        initPaint(context)
        initAnimator()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        initPaint(context)
        initAnimator()
    }

    /**
     * 画笔属性
     */
    private fun initPaint(context: Context) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        wm.defaultDisplay.getSize(point)
        screenWidth = point.x.toFloat()
        screenHeight = point.y.toFloat()
        paint.color = ContextCompat.getColor(context, R.color.color_35FF0000)
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.strokeWidth = 0f
        pathList = ArrayList()
        setPaths(3)
        if (showHeight == 0f) {
            showHeight = 100f
        }

        mWaveCount = Math.round(screenWidth / mWaveWidth + 1.5).toInt()
    }

    /**
     * 动画/m
     */
    private fun initAnimator() {
        animator.setFloatValues(0f, mWaveWidth)
        animator.duration = speed.toLong()
        animator.repeatCount = -1
        animator.interpolator = LinearInterpolator()

        animator.addUpdateListener {
            val change = it.animatedValue as Float
            xOffset = change
//            createShader()
            invalidate()
        }
        animator.start()
    }

    private fun setPaths(num: Int) {
        pathList.clear()
        for (i in 0 until num) {
            pathList.add(Path())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        canvas?.apply {
            createWaves(canvas, mWaveWidth / 5)
        }
    }

    /**
     * 创建多条波浪线
     */
    private fun createWaves(canvas: Canvas, offset: Float) {

        for (i in 0 until pathList.size) {
            pathList[i].apply {
                //清空之前绘制的路径
                reset()

                moveTo((-mWaveCount * 3 / 4f), showHeight)
                for (j in 0..mWaveCount) {
                    quadTo(
                        -mWaveWidth * 3 / 4 + i * mWaveWidth + xOffset + offset * i,
                        showHeight + mWaveHeight,
                        -mWaveWidth / 2 + i * mWaveWidth + xOffset + offset * i,
                        showHeight
                    )
                    quadTo(
                        -mWaveWidth / 4 + i * mWaveWidth + xOffset + offset * i,
                        showHeight - mWaveHeight,
                        i * mWaveWidth + xOffset + offset * i,
                        showHeight
                    )
                }

                lineTo(screenWidth + xOffset + offset * i, screenHeight)
                lineTo(xOffset + offset * i - screenWidth, screenHeight)
                lineTo(xOffset + offset * i - screenWidth, showHeight)
                canvas.drawPath(this, paint)
            }
        }


    }

}
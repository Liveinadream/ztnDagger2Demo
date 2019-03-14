package com.ztn.commom.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
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

    private var screenWidth = 0f
    private var screenHeight = 0f

    private var showHeight = 0f             //波浪中间高度所在位置
    private var xOffset = 0f                //波随时间的偏移量
    private var speed = 1000f               //波的速度 2s完成一个循环
    private var mWaveWidth = 500f           //波的宽度
    private var mWaveHeight = 100f          //波的高度
    private var mWaveCount = 0              //波的数量
    private var wavesNum = 3               //几条波


    private var animator = ValueAnimator()


    constructor(context: Context) : super(context) {
        initPaint(context)
        initWave()
        initAnimator()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        initPaint(context)
        initWave()
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
    }

    /**
     * 初始化波
     */
    private fun initWave() {
        pathList = ArrayList()
        setWaveNums(wavesNum)
        if (showHeight == 0f) {
            showHeight = 100f
        }

        mWaveCount = Math.round(screenWidth / mWaveWidth + 2.5).toInt()
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

    /**
     * 设置波的数量
     */
    private fun setWaveNums(num: Int) {
        wavesNum = num
        pathList.clear()
        for (i in 0 until num) {
            pathList.add(Path())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            createWaves(this, mWaveWidth / 5)
        }

    }

    /**
     * 创建多条波浪线
     */
    private fun createWaves(canvas: Canvas, offset: Float) {

        for (pathNum in 0 until pathList.size) {
            pathList[pathNum].apply {
                //清空之前绘制的路径
                reset()

                moveTo((-mWaveCount * 3f / 4f) - offset * pathNum, showHeight)

                for (i in 0..mWaveCount) {
                    quadTo(
                        -mWaveWidth * 3 / 4 + i * mWaveWidth + xOffset - offset * pathNum,
                        showHeight + mWaveHeight,
                        -mWaveWidth / 2 + i * mWaveWidth + xOffset - offset * pathNum,
                        showHeight
                    )
                    quadTo(
                        -mWaveWidth / 4 + i * mWaveWidth + xOffset - offset * pathNum,
                        showHeight - mWaveHeight,
                        i * mWaveWidth + xOffset - offset * pathNum,
                        showHeight
                    )
                }

                //铺满波浪线下方,形成封闭区
                lineTo(screenWidth, screenHeight)
                lineTo(0f, screenHeight)
                close()

                canvas.drawPath(this, paint)
            }
        }
    }




}
package com.ztn.commom.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.ztn.common.utils.dip2px

/**
 * Created by 冒险者ztn on 2019/3/15.
 * 一个圆形 view
 */
class RoundViewUseCanvas : View {

    private var smallRoundWidth = 15f.dip2px               //小圆点宽度
    private var dia = 180f.dip2px
    private val arcMargin = 20f.dip2px
    private val fix = 1f.dip2px         //小圆点修正
    private val speed = 2000

    private var progressAnimator = ValueAnimator()              //进度条动画
    private var score = 50f                                     //最终分数
    private var showScore = 0f                                  //动画展示分数

    //几种画笔
    private var arcPaint = Paint() //大圆画笔
    private var arcScorePaint = Paint() //小圆画笔
    private var roundPaint = Paint() //小圆画笔
    private var textPaint = Paint()

    private var smallRoundPath = Path()
    private var arc: RectF? = null
    private var canvas: Canvas? = null


    constructor(context: Context) : super(context) {
        initPaint()
        initAnimator()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initPaint()
        initAnimator()
    }


    /**
     * 初始化画笔属性
     */
    private fun initPaint() {
        arcPaint.isAntiAlias = true
        arcPaint.strokeWidth = smallRoundWidth
        arcPaint.style = Paint.Style.STROKE
        arcPaint.color = Color.RED
        arcPaint.strokeCap = Paint.Cap.ROUND

        arcScorePaint.isAntiAlias = true
        arcScorePaint.strokeWidth = smallRoundWidth
        arcScorePaint.style = Paint.Style.STROKE
        arcScorePaint.color = Color.WHITE
        arcScorePaint.strokeCap = Paint.Cap.ROUND

        roundPaint.isAntiAlias = true
        roundPaint.strokeWidth = 0f
        roundPaint.style = Paint.Style.FILL

    }


    /**
     * 动画/m
     */
    private fun initAnimator() {
        progressAnimator.setFloatValues(0f, score)
        progressAnimator.duration = speed.toLong()
        progressAnimator.interpolator = LinearInterpolator()

        progressAnimator.addUpdateListener {
            val change = it.animatedValue as Float
            showScore = change
            invalidate()
        }
        progressAnimator.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.canvas = canvas
        canvas?.apply {
            drawArc(this)
            drawScore(this)
            drawSmallRound(this)
        }

    }

    //画大圆弧
    private fun drawArc(canvas: Canvas) {

        //要绘制的矩形
        if (arc == null) {
            arc = RectF(
                canvas.width / 2f - dia / 2,
                arcMargin,
                canvas.width / 2f + dia / 2,
                dia + arcMargin
            )
        }

        canvas.drawArc(arc!!, 150f, 240f, false, arcPaint)

    }

    //画分数弧
    private fun drawScore(canvas: Canvas) {
        if (showScore == 0f) {
            canvas.drawArc(arc!!, 150f, 0.1f, false, arcScorePaint)
        } else {
            canvas.drawArc(arc!!, 150f, 240f / 100f * showScore, false, arcScorePaint)
        }
    }

    //画小圆点
    private fun drawSmallRound(canvas: Canvas) {
        val endX =
            canvas.width / 2 + (dia / 2) * Math.cos(Math.toRadians((240f / 100f * showScore + 150).toDouble()))
        val endY =
            canvas.height / 2 - arcMargin - 1.5 * smallRoundWidth + (dia / 2) * Math.sin(Math.toRadians((240f / 100f * showScore + 150).toDouble())) + 2.5 * fix
        canvas.drawCircle(endX.toFloat(), endY.toFloat(), smallRoundWidth / 2, roundPaint)
    }

    /**
     * 设置分数
     */
    fun setScore(score: Float) {
        this.score = score
        progressAnimator = ValueAnimator()
        showScore = 0f
        initAnimator()

    }


}
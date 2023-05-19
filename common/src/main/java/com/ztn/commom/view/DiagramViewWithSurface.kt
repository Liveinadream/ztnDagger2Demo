package com.ztn.commom.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.opengl.GLSurfaceView
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import com.orhanobut.logger.Logger
import com.ztn.commom.R
import com.ztn.common.utils.lock
import com.ztn.common.utils.notifyAll
import com.ztn.common.utils.wait
import com.ztn.library.rx.CommonOnSubscribe
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.disposables.CompositeDisposable
import java.util.*

/**
 * Created by 冒险者ztn on 2019/3/14.
 * 曲线图
 */
class DiagramViewWithSurface : SurfaceView, Runnable {

    private var paint = Paint()
    private var paint2 = Paint()
    private var pathList = Vector<Path>()

    private var screenWidth = 0f
    private var screenHeight = 0f

    private var showHeight = 0f             //波浪中间高度所在位置
    private var xOffset = 0f                //波随时间的偏移量
    private var speed = 5000f               //波的速度 1s完成一个循环
    private var mWaveWidth = 500f           //波的宽度
    private var mWaveHeight = 100f          //波的高度
    private var mWaveCount = 0              //波的数量
    private var wavesNum = 3                //几条波
    private lateinit var background: Bitmap
    private var flag = true


    private var animator = ValueAnimator()
    private var compositeDisposable = CompositeDisposable()


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

        paint2.color = Color.WHITE

        background = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.dir
        )
    }

    /**
     * 初始化波
     */
    private fun initWave() {
        pathList = Vector()
        setWaveNums(wavesNum)
        if (showHeight == 0f) {
            showHeight = 100f
        }

        mWaveCount = Math.round(screenWidth / mWaveWidth + 2.5).toInt()
    }

    /**
     * 动画
     */
    private fun initAnimator() {
        animator.setFloatValues(0f, mWaveWidth)
        animator.duration = speed.toLong()
        animator.repeatCount = -1
        animator.interpolator = LinearInterpolator()

        animator.addUpdateListener {
            val change = it.animatedValue as Float
            xOffset = change
            invalidate()
        }
        animator.start()
    }

    /**
     * 设置波的数量
     */
    fun setWaveNums(num: Int) {
        wavesNum = num
        pathList.clear()
        clear()
        for (i in 0 until num) {
            pathList.add(Path())
        }

    }

    /**
     * 设置波的宽度
     */
    fun setWaveWidth(width: Float) {
        mWaveWidth = width
        mWaveCount = Math.round(screenWidth / mWaveWidth + 2.5).toInt()
        setWaveNums(wavesNum)
    }

    override fun run() {
        while (true) {

            if (flag) {
                if (!holder.surface.isValid) {
                    continue
                }
                val canvas = holder.lockCanvas()
                if (canvas != null) {
                    canvas.drawBitmap(background, 0f, 0f, paint2)
                    clear()
                    createWaves(canvas, mWaveWidth / 5)
                    holder.unlockCanvasAndPost(canvas)
                }
            } else {
                synchronized(this){
                    wait()
                }
            }

        }
    }


    /**
     * 创建多条波浪线
     */
    private fun createWaves(canvas: Canvas, offset: Float) {

        synchronized(pathList) {
            for (pathNum in 0 until pathList.size) {
                pathList[pathNum].apply {
                    val path = this

                    val dispose = Observable.create(object : CommonOnSubscribe<Path>() {
                        override fun work(e: ObservableEmitter<Path>) {
                            path.apply {
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
                            }
                            e.onNext(path)
                            e.onComplete()

                        }
                    }).subscribe({
                        canvas.drawPath(it, paint)

                    }, {
                        Logger.e(it.message)
                    })

                    compositeDisposable.add(dispose)
                }
            }
        }

    }

    private fun clear() {

        //保证activity结束时取消所有正在执行的订阅
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
    }

    fun pause() {
        flag = false
    }

    fun resume() {
        flag = true
        lock {
            notifyAll()
        }
    }


}
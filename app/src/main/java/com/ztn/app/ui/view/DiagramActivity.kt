package com.ztn.app.ui.view

import android.content.Context
import android.content.Intent
import com.orhanobut.logger.Logger
import com.ztn.app.R
import com.ztn.app.base.SimpleActivity
import com.ztn.common.ToastHelper
import kotlinx.android.synthetic.main.activity_diagram.*

/**
 * Created by 冒险者ztn on 2019/3/14.
 * 曲线图
 */
class DiagramActivity : SimpleActivity() {

    companion object {
        fun startWithNothing(context: Context) {
            context.startActivity(Intent(context, DiagramActivity::class.java))
        }
    }

    private var onDraw = false

    override fun getLayout(): Int {
        return R.layout.activity_diagram
    }

    override fun initEventAndData() {
    }

    override fun onViewCreated() {
        super.onViewCreated()
        activityTitle.text = "曲线图"
        try {
            Thread {
                diagramView.run()
            }.start()
        } catch (e: InterruptedException) {
            Logger.e(e.message)

        } catch (e: IllegalStateException) {
            Logger.e(e.message)

        }

//        diagramView.setWaveNums(1)

        roundView.setScore(20f)

        sure.setOnClickListener {

            if (waveNum.text.toString().toInt() <= 0) {
                ToastHelper.showToast("至少一条线")
            } else {
                diagramView.setWaveNums(waveNum.text.toString().toInt())
            }

//            diagramView. setAnimatorReverse()
            if (score.text.toString().toFloat() in 0.0..100.0) {
                roundView.setScore(score.text.toString().toFloat())
            } else {
                ToastHelper.showToast("分数不合理")
            }
            onDraw = false
        }

    }

    override fun onPause() {
        super.onPause()
        diagramView.pause()
    }

    override fun onRestart() {
        super.onRestart()
        diagramView.resume()
    }
}
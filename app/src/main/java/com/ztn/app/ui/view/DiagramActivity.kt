package com.ztn.app.ui.view

import android.content.Context
import android.content.Intent
import com.ztn.app.R
import com.ztn.app.base.SimpleActivity
import com.ztn.common.ToastHelper
import kotlinx.android.synthetic.main.activity_diagram.*
import kotlinx.android.synthetic.main.base_activity_title.*

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


    override fun getLayout(): Int {
        return R.layout.activity_diagram
    }

    override fun initEventAndData() {
    }

    override fun onViewCreated() {
        super.onViewCreated()
        activityTitle.text = "曲线图"

        diagramView.setWaveNums(1)

        roundView.setScore(20f)

        sure.setOnClickListener {
            diagramView.setWaveNums(waveNum.text.toString().toInt())
            diagramView. setAnimatorReverse()
            if (score.text.toString().toFloat() in 0.0..100.0) {
                roundView.setScore(score.text.toString().toFloat())
            } else {
                ToastHelper.showToast("分数不合理")
            }
        }
    }

}
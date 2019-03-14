package com.ztn.app.ui.view

import android.content.Context
import android.content.Intent
import com.ztn.app.R
import com.ztn.app.base.SimpleActivity
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

    }

}
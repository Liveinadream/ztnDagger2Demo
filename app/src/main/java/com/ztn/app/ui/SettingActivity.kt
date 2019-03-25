package com.ztn.app.ui

import android.os.Bundle
import com.ztn.app.R
import com.ztn.app.base.SimpleActivity
import com.ztn.app.fragment.setting.LogoSettings

/**
 * Created by 冒险者ztn on 2019/2/20.
 * 设置界面
 */
class SettingActivity : SimpleActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_setting
    }

    override fun initEventAndData() {
        activityTitle.text = "设置"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().replace(R.id.content, LogoSettings()).commit()
    }

}
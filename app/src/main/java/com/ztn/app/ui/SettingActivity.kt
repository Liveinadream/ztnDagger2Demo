package com.ztn.app.ui

import android.os.Bundle
import android.util.Log
import com.ztn.app.R
import com.ztn.app.adapter.FileAdapterEnum
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().replace(R.id.content, LogoSettings()).commit()


        val a = add({ add(1, 2) }, { 2 + 3 })

        Log.d(
            "得到的adapter数量：", "${FileAdapterEnum.INSTANCE.getNum()}"
        )
    }

//    tailrec fun findFixPoint(x: Double = 1.0): Double = if (x == Math.cos(x)) x else findFixPoint(Math.cos(x))

    private inline fun add(sumOne: () -> Int, sumTwo: () -> Int): Int {
        return sumOne.invoke() + sumTwo.invoke()
    }

    private inline fun fun2(body: () -> Unit) {

    }

    private fun add(sumOne: Int, sumTwo: Int): Int {
        return sumOne + sumTwo
    }

    private fun test() {
        var a = 1
        var b = 5

        if (a >= b) {
            add({ add(a, b) }, { b })
        } else {
            add(b, a)
        }
    }

}
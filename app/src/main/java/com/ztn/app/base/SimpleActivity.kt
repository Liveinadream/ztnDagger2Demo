package com.ztn.app.base

import android.os.Bundle
import android.widget.TextView
import com.ztn.app.R
import com.ztn.common.framework.AppManager
import me.yokeyword.fragmentation.SupportActivity

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 不需要使用 MVP 的 activity 继承该类
 *
 */
abstract class SimpleActivity : SupportActivity() {

    lateinit var activityTitle: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        onViewCreated()
        AppManager.get().addActivity(this)
        initEventAndData()
    }

    override fun onDestroy() {
        super.onDestroy()

        AppManager.get().removeActivity(this)

    }

    protected open fun onViewCreated() {
        activityTitle = findViewById(R.id.activityTitle)

    }

    protected abstract fun getLayout(): Int
    protected abstract fun initEventAndData()


}
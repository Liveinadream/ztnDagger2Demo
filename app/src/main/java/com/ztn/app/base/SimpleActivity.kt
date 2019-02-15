package com.ztn.app.base

import android.os.Bundle
import com.ztn.common.framework.AppManager
import me.yokeyword.fragmentation.SupportActivity

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 不需要使用 MVP 的 activity 继承该类
 * */
abstract class SimpleActivity : SupportActivity() {

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

    }

    protected abstract fun getLayout(): Int
    protected abstract fun initEventAndData()


}
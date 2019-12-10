package com.ztn.app.base

import com.ztn.app.di.component.AppComponent
import com.ztn.app.di.component.DaggerAppComponent
import com.ztn.app.di.module.ApplicationModule
import com.ztn.app.util.LogUtils
import com.ztn.common.framework.AppManager
import com.ztn.common.framework.CrashHandler
import net.sf.jmimemagic.Magic
import javax.inject.Inject

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 基本的 application
 */
class BaseApplication : me.goldze.mvvmhabit.base.BaseApplication() {
    var appComponent: AppComponent? = null

    @Inject
    lateinit var app: BaseApplication

    override fun onCreate() {
        super.onCreate()

        app = this
        instance = this
        //初始化 ranger 库中的 context
        AppManager.context = this

        //初始化奔溃日志,需要写入权限
        CrashHandler.instance.init(this)
        appComponent = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(app))
//            .httpModule(HttpModule())
            .build()

        LogUtils.init()
        Thread {
            Magic.initialize()
        }.start()

    }

    companion object {
        lateinit var instance: BaseApplication
    }


    /**
     * 伴随应用启动仅仅运行一次并且在主进程上的程序
     */
    fun runOnlyOnce() {

        if (true) {

        }
    }

}
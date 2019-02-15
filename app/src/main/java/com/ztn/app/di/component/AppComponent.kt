package com.ztn.app.di.component

import com.ztn.app.base.BaseApplication
import com.ztn.app.di.module.ApplicationModule
import com.ztn.app.di.module.HttpModule
import com.ztn.app.di.module.RxThreadModule
import com.ztn.app.ui.LoginActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
@Singleton
@Component(modules = [ ApplicationModule::class, HttpModule::class, RxThreadModule::class])
interface AppComponent {
    fun inject(app: BaseApplication)

    fun inject(activity: LoginActivity)

}

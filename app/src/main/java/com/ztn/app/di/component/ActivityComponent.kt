package com.ztn.app.di.component

import android.app.Activity
import com.ztn.app.di.module.ActivityModule
import com.ztn.app.di.scope.ActivityScope
import com.ztn.app.ui.LoginActivity
import dagger.Component

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun getActicity(): Activity

    fun inject(loginActivity: LoginActivity)
}

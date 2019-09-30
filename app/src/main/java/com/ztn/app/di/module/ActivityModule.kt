package com.ztn.app.di.module

import android.app.Activity
import com.ztn.app.di.scope.ActivityScope
import com.ztn.app.ui.LoginActivity
import dagger.Module
import dagger.Provides

/**
 * Created by 冒险者ztn on 2019/2/12.
 *
 * module 提供你的对象，也就是你用到的对象，初始化你要的 presenter 的时候，或者全局用到的对象
 * 提供 BaseActivity 的 module
 * 注入Activity，同时规定 Activity 所对应的域是 [ActivityScope]
 */
@Module
class ActivityModule(private var mActivity: Activity) {

    @Provides
    @ActivityScope
    fun provideActivity(): Activity = mActivity

    @Provides
    @ActivityScope
    fun provideLoginActivity(): LoginActivity = mActivity as LoginActivity


}



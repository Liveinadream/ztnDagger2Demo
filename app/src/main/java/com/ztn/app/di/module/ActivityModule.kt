package com.ztn.app.di.module

import android.app.Activity
import com.ztn.app.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
@Module
class ActivityModule(private val mActivity: Activity) {

    @Provides
    @ActivityScope
    fun provideActivity(): Activity = mActivity

//    @Provides
//    fun providePresenter(): LoginContract.Present = LoginPresenter()
}
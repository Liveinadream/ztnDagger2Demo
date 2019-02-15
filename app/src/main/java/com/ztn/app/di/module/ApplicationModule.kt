package com.ztn.app.di.module

import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.ztn.app.base.BaseApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
@Module
class ApplicationModule(private val application: BaseApplication) {

    @Provides
    @Singleton
    internal fun provideApplication(): BaseApplication = application

    @Provides
    @Singleton
    fun provideInputMethod() = application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

}
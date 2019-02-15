package com.ztn.app.di.module

import android.app.Activity
import android.support.v4.app.Fragment
import com.ztn.app.di.scope.ActivityScope
import com.ztn.app.di.scope.FragmentScope
import dagger.Module
import dagger.Provides

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    @FragmentScope
    fun provideActivity(): Activity? {
        return fragment.activity
    }
}

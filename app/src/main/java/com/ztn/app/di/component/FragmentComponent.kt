package com.ztn.app.di.component

import com.ztn.app.di.module.FragmentModule
import com.ztn.app.di.module.HttpModule
import com.ztn.app.di.module.RxThreadModule
import com.ztn.app.di.scope.FragmentScope
import com.ztn.app.rx.RxThread
import com.ztn.app.fragment.FileFragment
import dagger.Component

/**
 * Created by 冒险者ztn on 2019/3/12.
 * 与fragment 生命周期相同的组建
 */

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [FragmentModule::class, HttpModule::class, RxThreadModule::class]
)
interface FragmentComponent {

    fun inject(fileFragment: FileFragment)

    fun inject(rxThread: RxThread)

}
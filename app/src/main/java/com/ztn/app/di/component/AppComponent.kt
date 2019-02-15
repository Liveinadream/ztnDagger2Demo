package com.ztn.app.di.component

import com.ztn.app.base.BaseApplication
import com.ztn.app.di.module.ApplicationModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by 冒险者ztn on 2019/2/12.
 * AppComponent: 生命周期跟 Application 一样的组件。可注入到自定义的Application类中，@Singleton代表各个注入对象为单例。
 */
@Singleton
@Component(modules = [ApplicationModule::class])
interface AppComponent {

    fun inject(app: BaseApplication)


//    val context: App  // 提供App的Context
//
//    val dataManager: DataManager //数据中心
//
//    fun retrofitHelper(): RetrofitHelper   //提供http的帮助类
//
//    fun realmHelper(): RealmHelper     //提供数据库帮助类

}
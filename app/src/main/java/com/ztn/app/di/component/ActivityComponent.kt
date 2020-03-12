package com.ztn.app.di.component

import com.ztn.app.di.module.ActivityModule
import com.ztn.app.di.module.HttpModule
import com.ztn.app.di.module.RxThreadModule
import com.ztn.app.di.scope.ActivityScope
import com.ztn.network.interfaces.ZhihuApis
import com.ztn.app.rx.RxThread
//import com.ztn.app.ui.LoginActivity
import com.ztn.app.ui.chat.FriendActivity
import com.ztn.app.ui.file.FileActivity
import dagger.Component

/**
 * Created by 冒险者ztn on 2019/2/12.
 *
 * @ActivityScope 生命周期跟Activity一样的组件，
 * 这里提供了inject方法将Activity注入到ActivityComponent中，
 * 通过该方法，将Activity中需要注入的对象注入到该Activity中
 *
 */
@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [ActivityModule::class, HttpModule::class, RxThreadModule::class]
)
interface ActivityComponent {

//    fun inject(loginActivity: LoginActivity)

    fun inject(fileActivity: FileActivity)

    fun inject(friendActivity: FriendActivity)

    fun inject(apis: ZhihuApis)

    fun inject(rxThread: RxThread)

}

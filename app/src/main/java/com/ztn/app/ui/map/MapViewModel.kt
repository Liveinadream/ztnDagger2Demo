package com.ztn.app.ui.map

import android.app.Application
import com.gomap.sdk.maps.MapView
import com.ztn.app.data.DemoRepository
import me.goldze.mvvmhabit.base.BaseViewModel

/**
 * Created by 冒险者ztn on 2019-10-07.
 * 登陆界面
 */
class MapViewModel(application: Application, demoRepository: DemoRepository?) :
    BaseViewModel<DemoRepository?>(application, demoRepository) {
    @JvmField
    var title = "地图界面"
}
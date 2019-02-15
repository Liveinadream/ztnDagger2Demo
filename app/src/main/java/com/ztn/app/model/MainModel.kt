package com.ztn.app.model

import com.ztn.app.model.bean.WelcomeBean

/**
 * Created by 冒险者ztn on 2019/2/13.
 * 介绍 todo
 */
class MainModel {
    interface CallBack {
        fun data(info: WelcomeBean)
    }

    fun getInfo(callBack: CallBack) {
        callBack.data(WelcomeBean("dasd", "www.baidu.com"))
    }
}
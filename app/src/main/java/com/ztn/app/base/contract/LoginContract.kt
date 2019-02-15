package com.ztn.app.base.contract

import com.ztn.app.base.BasePresenter
import com.ztn.app.base.BaseView
import com.ztn.app.base.IPresenter

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
interface LoginContract {
    interface View : BaseView {
        fun loginSuccess()

    }

    interface Present : IPresenter<View> {
        fun login()

    }
}
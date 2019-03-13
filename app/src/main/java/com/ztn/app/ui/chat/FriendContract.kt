package com.ztn.app.ui.chat

import com.ztn.app.base.BaseView
import com.ztn.app.base.IPresenter

/**
 * Created by 冒险者ztn on 2019/3/13.
 */
interface FriendContract {
    interface View : BaseView {

        fun showChatList()

    }

    interface Presenter : IPresenter<View> {

        fun getChatData()

    }
}
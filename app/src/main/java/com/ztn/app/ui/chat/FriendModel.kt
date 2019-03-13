package com.ztn.app.ui.chat

import com.ztn.network.bean.ChatBean
import com.ztn.network.interfaces.ZtnService
import io.reactivex.Observable

/**
 * Created by 冒险者ztn on 2019/3/13.
 */
class FriendModel(private val api: ZtnService) {

    fun loadData(userId: String): Observable<ChatBean> {
        return api.getWelcomeInfo(userId)
    }
}
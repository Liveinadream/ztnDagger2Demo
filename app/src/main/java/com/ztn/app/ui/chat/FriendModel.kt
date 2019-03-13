package com.ztn.app.ui.chat

import com.ztn.app.model.bean.ChatBean
import com.ztn.app.model.http.api.ChatApis
import io.reactivex.Observable

/**
 * Created by 冒险者ztn on 2019/3/13.
 */
class FriendModel(private val api: ChatApis) {

    fun loadData(userId: String): Observable<ChatBean> {
        return api.getWelcomeInfo(userId)
    }
}
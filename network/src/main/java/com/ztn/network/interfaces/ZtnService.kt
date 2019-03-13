package com.ztn.network.interfaces

import com.ztn.network.bean.ChatBean
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by 冒险者ztn on 2019/3/13.
 * 介绍 todo
 */
interface ZtnService{

    /**
     * 获取最近聊天信息
     */
    @POST("chat/getChat")
    @FormUrlEncoded
    fun getWelcomeInfo(@Field("userId") userId: String): Observable<ChatBean>

    companion object {

        //        const val HOST = "http://news-at.zhihu.com/api/4/"
        const val HOST = "http://news-at.zhihu.com/api/4/"
    }
}
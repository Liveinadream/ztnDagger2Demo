package com.ztn.app.model.http.api

import com.ztn.app.model.bean.WelcomeBean
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
interface ZhihuApis {

    /**
     * 启动界面图片
     */
    @GET("start-image/{res}")
    fun getWelcomeInfo(@Path("res") res: String): Observable<WelcomeBean>

    companion object {

        const val HOST = "http://news-at.zhihu.com/api/4/"
    }

}

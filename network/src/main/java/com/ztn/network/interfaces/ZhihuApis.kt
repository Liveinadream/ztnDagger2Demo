package com.ztn.network.interfaces

import com.ztn.network.bean.WelcomeBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by 冒险者ztn on 2019/2/12.
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

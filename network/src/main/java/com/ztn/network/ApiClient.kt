package com.ztn.network

import com.ztn.common.framework.AppManager
import com.ztn.network.interfaces.ZtnService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient private constructor() {
    lateinit var service: ZtnService
    var interceptorList = ArrayList<Interceptor>()

    private object Holder {
        val INSTANCE = ApiClient()
    }

    companion object {
        const val DEFAULT_TIMEOUT: Long = 15
        const val MAX_CACHE_SIZE: Long = 1024 * 1024 * 50 // 50M 的缓存大小

        val instance by lazy { Holder.INSTANCE }
    }

    fun addInterceptor(interceptor: Interceptor) {
        interceptorList.add(interceptor)
    }

    fun init() {
        //设置 请求的缓存的大小跟位置
//        val cacheFile = File(AppManager.context.cacheDir, "cache")
//        val cache = Cache(cacheFile, MAX_CACHE_SIZE)

        val okHttpClientBuilder = OkHttpClient().newBuilder()
            .retryOnConnectionFailure(false)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)

        for (interceptor in interceptorList) {
            okHttpClientBuilder.addInterceptor(interceptor)
        }
        val okHttpClient = okHttpClientBuilder
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                )
            ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ZtnService.HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        service = retrofit.create(ZtnService::class.java)
    }
}

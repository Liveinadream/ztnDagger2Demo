package com.ztn.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class UserAgentInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authorised = originalRequest.newBuilder()
            .header("Connection", "Keep-alive")
            .header("Keep-Alive", "timeout=20")
            .build()
        return chain.proceed(authorised)
    }
}
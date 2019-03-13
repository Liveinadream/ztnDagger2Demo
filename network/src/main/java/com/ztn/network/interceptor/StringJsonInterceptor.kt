package com.ztn.network.interceptor

import com.ztn.network.GsonUtil
import com.ztn.network.interfaces.ZtnService
import com.ztn.network.json
import okhttp3.*
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URLDecoder

/**
 * Created by 冒险者ztn on 2018/11/30.
 *
 */
class StringJsonInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        val method = request.method()
        val mHttpUrl = request.url()

        if (mHttpUrl.toString().contains(ZtnService.HOST) && "GET" == method) {
            request = nodeGetApi(request, mHttpUrl)
        } else if (mHttpUrl.toString().contains(ZtnService.HOST) && "POST" == method) {
            request = nodePostApi(request)
        }

        return chain.proceed(request)
    }

    private fun nodeGetApi(request: Request, mHttpUrl: HttpUrl): Request {
        val rootMap = HashMap<String, Any>()

        val paramNames = mHttpUrl.queryParameterNames()
        for (key in paramNames) {
            val oldParamJson = mHttpUrl.queryParameter(key)

            oldParamJson?.apply {
                try {
                    val p = GsonUtil.getInstance().fromJson<HashMap<String, Any>>(this, HashMap::class.java)
                    if (p != null) {
                        for (entry in p.entries) {
                            rootMap[entry.key] = entry.value.toString().json
                        }
                    }
                } catch (e: Exception) {
                    rootMap[key] = json
                }

            }

        }
        val paramString = StringBuilder("")

        for (entry in rootMap) {
            paramString.append(entry.key)
            paramString.append("=")
            paramString.append(entry.value)
            paramString.append("&")
        }
        val useParamsJson = paramString.substring(0, paramString.length - 1)
        var url = mHttpUrl.toString()

        val index = url.indexOf("?")
        if (index > 0) {
            url = url.substring(0, index)
        }

        url = "$url?$useParamsJson"
        return request.newBuilder().url(url).build()
    }

    private fun nodePostApi(request: Request): Request? {
        val body = request.body()

        if (body != null) {
            when (body) {
                is FormBody -> {
                    val formBodyBuilder = FormBody.Builder()

                    for (i in 0 until body.size()) {
                        formBodyBuilder.add(body.encodedName(i), URLDecoder.decode(body.encodedValue(i), "UTF-8").json)
                    }
                    return request.newBuilder().post(formBodyBuilder.build()).build()

                }
                is MultipartBody -> {
//                    val multipartBodyBuilder = MultipartBody.Builder()
//
//                    for (part in body.parts()) {
//                        val partBody = part.body()
//                        val headers = part.headers()
//                        headers?.apply {
//                            for (head in headers.names()) {
//                                Logger.d("键：$head  值：${headers[head]}")
//                            }
//                        }
//
//                        val buffer = Buffer()
//                        partBody?.writeTo(buffer)
//                        when {
//                            partBody.contentType() == FROM_DATA -> {
//                                Logger.d("构建一个 FROM_DATA")
//                                multipartBodyBuilder.addPart(MultipartBody.Part.create(headers, RequestBody.create(partBody.contentType(), buffer.readUtf8())))
//                            }
//                            partBody.contentType() == JSON -> {
//                                Logger.d("构建一个 JSON")
//
//                                multipartBodyBuilder.addPart(MultipartBody.Part.create(headers, RequestBody.create(partBody.contentType(), buffer.readUtf8())))
//                            }
//                            partBody.contentType() == URL_ENCODED -> {
//                                Logger.d("构建一个 URL_ENCODED")
//
//                                multipartBodyBuilder.addPart(MultipartBody.Part.create(headers, RequestBody.create(partBody.contentType(), buffer.readUtf8().json)))
//                            }
//                            partBody.contentType() == null -> {
//                                Logger.d("构建一个 null")
//
//                                multipartBodyBuilder.addPart(MultipartBody.Part.create(headers, RequestBody.create(partBody.contentType(), buffer.readUtf8().json)))
//                            }
//                            else -> {
//                                Logger.d("构建一个 其他")
//                                multipartBodyBuilder.addPart(MultipartBody.Part.create(headers, RequestBody.create(partBody.contentType(), buffer.readUtf8())))
//                            }
//                        }
//
//                    }
                    return request.newBuilder().post(body).build()
                }
                else -> {
                    return request.newBuilder().post(body).build()
                }
            }
        }
        return null

    }

}
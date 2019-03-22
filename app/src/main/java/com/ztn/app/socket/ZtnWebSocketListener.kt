package com.ztn.app.socket

import com.orhanobut.logger.Logger
import com.ztn.app.ui.chat.FriendActivity
import com.ztn.network.GsonUtil
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by 冒险者ztn on 2019/3/20.
 * WebSock 监听
 */
open class ZtnWebSocketListener(private val activity: FriendActivity) : WebSocketListener() {

    var webSocket: WebSocket? = null


    private fun outputMessage(msg: String) {
        activity.apply {
            runOnUiThread {
                Logger.d(msg)
                addShowMsg(msg)
            }
        }
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        this.webSocket = webSocket
        outputMessage("连接成功 \n$response")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        outputMessage("closing:  $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        outputMessage("Closed:   $reason")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                this@ZtnWebSocketListener.webSocket?.send(sendHeart())
            }

        }, 2000)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        outputMessage("failure:    ${t.message}\n ${response.toString()}")
    }

    fun sendHeart(): String {
        val mapHead = HashMap<String, Any>()
        mapHead["heart"] = "heart"
        return buildRequestParams(mapHead)
    }

    fun sendData(): String {
        val mapHead = HashMap<String, Any>()
        mapHead["qrCode"] = "123456"
        return buildRequestParams(mapHead)
    }

    companion object {

        fun buildRequestParams(params: Any): String {
            return GsonUtil.getInstance().toJson(params)
        }
    }


}
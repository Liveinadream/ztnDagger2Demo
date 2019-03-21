package com.ztn.app.ui.chat

import android.os.Handler
import com.ztn.app.R
import com.ztn.app.base.BaseActivity
import com.ztn.app.socket.ZtnWebSocketListener
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

/**
 * Created by 冒险者ztn on 2019/3/13.
 */
class FriendActivity : BaseActivity<FriendPresenter>(), FriendContract.View {

    companion object {
        const val HEART_BEAT_RATE = 2 * 1000L
    }

    lateinit var socketListener: ZtnWebSocketListener

    private var mHandler: Handler? = null
    private var sendTime = 0L
    private var mWebSocket: WebSocket? = null


    override fun initInject() {
        getActivityComponent().inject(this)
    }

    override fun getLayout(): Int {
        return R.layout.activity_files
    }


    override fun loading() {
    }

    override fun dismissLoading() {
    }

    override fun showChatList() {
    }

    override fun initEventAndData() {
        mPresenter.attachView(this)

        socketListener = object : ZtnWebSocketListener(this@FriendActivity) {}
        mWebSocket = socketListener.webSocket

    }

    override fun onViewCreated() {
        super.onViewCreated()

        mHandler = Handler()

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .connectTimeout(3, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder().url("192.168.1.102/WebSocket/api/message").build()

        mHandler?.postDelayed(heartBeatRunnable, HEART_BEAT_RATE)
        okHttpClient.newWebSocket(request, socketListener)
        okHttpClient.dispatcher().executorService().shutdown()

    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
        closeSocket()
    }

    private fun closeSocket() {
        if (mWebSocket != null) {
            mWebSocket?.close(1000, null)
        }

        if (mHandler != null) {
            mHandler?.removeCallbacksAndMessages(null)
            mHandler = null
        }
    }

    private val heartBeatRunnable = object : Runnable {
        override fun run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                mWebSocket?.send(socketListener.sendData())
                sendTime = System.currentTimeMillis()
            }
            mHandler?.postDelayed(this, HEART_BEAT_RATE)
        }
    }
}
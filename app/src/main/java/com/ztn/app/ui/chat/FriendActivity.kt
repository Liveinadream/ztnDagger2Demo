package com.ztn.app.ui.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import com.orhanobut.logger.Logger
import com.ztn.app.R
import com.ztn.app.base.BaseActivity
import com.ztn.app.socket.ZtnWebSocketListener
import com.ztn.network.interceptor.UserAgentInterceptor
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_friend.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.logging.HttpLoggingInterceptor
import java.net.URISyntaxException
import java.util.concurrent.TimeUnit


/**
 * Created by 冒险者ztn on 2019/3/13.
 */
class FriendActivity : BaseActivity<FriendPresenter>(), FriendContract.View, Emitter.Listener {


    companion object {
        const val HEART_BEAT_RATE = 2 * 1000L
        const val socketURI = "http://192.168.1.103:8080"
//        const val socketURI = "ws://192.168.1.146:8080"

        fun startWithNothing(context: Context) {
            context.startActivity(Intent(context, FriendActivity::class.java))
        }
    }

    lateinit var socketListener: ZtnWebSocketListener

    private var mSocketIO: Socket? = null
    lateinit var emitterListener: Emitter.Listener

    private var mHandler: Handler? = null
    private var sendTime = 0L
    private var mWebSocket: WebSocket? = null


    override fun initInject() {
        getActivityComponent().inject(this)
    }

    override fun getLayout(): Int {
        return R.layout.activity_friend
    }


    override fun loading() {
    }

    override fun dismissLoading() {
    }

    override fun showChatList() {
    }

    override fun initEventAndData() {
        mPresenter.attachView(this)
    }

    override fun onViewCreated() {
        super.onViewCreated()

//        webView.loadUrl("http://192.168.1.146:8080")
//        initWebSocket()
        initSocketIo()
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
        if (mSocketIO != null) {
            mSocketIO?.disconnect()
            mSocketIO = null
        }

    }

    @SuppressLint("SetTextI18n")
    fun addShowMsg(msg: String) {
        serverReturn.text = serverReturn.text.toString() + msg
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

    private fun initSocketIo() {
        emitterListener = this
        try {
//            val opts = IO.Options()
//            opts.sslContext = SSLContext.getDefault()
//            opts.hostnameVerifier = HostnameVerifier { _, _ ->
//                return@HostnameVerifier true
//            }

            mSocketIO = IO.socket(socketURI)
        } catch (e: URISyntaxException) {
            Logger.e(e.message)
        }

        mSocketIO?.on(Socket.EVENT_CONNECT, this)
        mSocketIO?.on(Socket.EVENT_DISCONNECT, this)
        mSocketIO?.on(Socket.EVENT_ERROR, this)
        mSocketIO?.on(Socket.EVENT_CONNECT_TIMEOUT, this)


        mSocketIO?.connect()
        mSocketIO?.on("sendMsg", emitterListener)

        sendMessage.setOnClickListener {
            mSocketIO?.emit("receiveMsg", "client", "android端", "msg", "hello")
        }
    }

    private fun initWebSocket() {
        socketListener = object : ZtnWebSocketListener(this@FriendActivity) {}
        mWebSocket = socketListener.webSocket

        mHandler = Handler()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(UserAgentInterceptor())
            .retryOnConnectionFailure(true)
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .connectTimeout(3, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder().url(socketURI).build()

        mHandler?.postDelayed(heartBeatRunnable, HEART_BEAT_RATE)
        okHttpClient.newWebSocket(request, socketListener)
        okHttpClient.dispatcher().executorService().shutdown()
    }

    override fun call(vararg args: Any?) {

        // add the message to view
        Logger.d("获得的消息$args")

        val data = args[0]

        Logger.d("获得的消息" + data.toString())

    }
}
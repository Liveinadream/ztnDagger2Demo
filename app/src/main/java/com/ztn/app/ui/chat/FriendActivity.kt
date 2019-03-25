package com.ztn.app.ui.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import com.orhanobut.logger.Logger
import com.ztn.app.R
import com.ztn.app.base.BaseActivity
import com.ztn.app.message.MsgBean
import com.ztn.app.message.NameValuePairs
import com.ztn.app.socket.ZtnWebSocketListener
import com.ztn.common.ToastHelper
import com.ztn.network.GsonUtil
import com.ztn.network.interceptor.UserAgentInterceptor
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_friend.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.concurrent.TimeUnit


/**
 * Created by 冒险者ztn on 2019/3/13.
 */
class FriendActivity : BaseActivity<FriendPresenter>(), FriendContract.View {


    companion object {
        const val HEART_BEAT_RATE = 2 * 1000L
        const val socketURI = "http://192.168.1.188:8080"
//        const val socketURI = "ws://192.168.1.146:8080"

        fun startWithNothing(context: Context) {
            context.startActivity(Intent(context, FriendActivity::class.java))
        }
    }

    private var socketState = Socket.EVENT_DISCONNECT
    lateinit var socketListener: ZtnWebSocketListener

    private var mSocketIO: Socket? = null

    private var mHandler: Handler? = null
    private var sendTime = 0L
    private var mWebSocket: WebSocket? = null
    private val msgObj = JSONObject()


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
        activityTitle.text = "IM"
    }

    @SuppressLint("SetTextI18n")
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
        try {
            val opts = IO.Options()
//            opts.sslContext = SSLContext.getDefault()
//            opts.hostnameVerifier = HostnameVerifier { _, _ ->
//                return@HostnameVerifier true
//            }
            opts.reconnection = false
            mSocketIO = IO.socket(socketURI, opts)
        } catch (e: URISyntaxException) {
            Logger.e(e.message)
        }

        initSocketIoListener()
        mSocketIO?.connect()

        sendMessage.setOnClickListener {

            val str = sendMessageText.text.toString()
            if (str.isNotEmpty()) {
                sendMessageInSocketIo(str)
                sendMessageText.setText("")
            } else {
                ToastHelper.showToast("消息内容为null")
            }

        }
    }

    private fun initSocketIoListener() {

        mSocketIO?.on(Socket.EVENT_CONNECT) {

            Logger.d("连接建立")
            socketState = Socket.EVENT_CONNECT
            sendMessageInSocketIo("Android端连接已经建立")

        }?.on(Socket.EVENT_DISCONNECT) {

            socketState = Socket.EVENT_CONNECT
            val str = it.map { any -> return@map any.toString() }
            Logger.d("连接结束$str")
            sendMessageInSocketIo("Android端的对话结束了")
            mSocketIO = null

        }?.on(Socket.EVENT_ERROR) {
            socketState = Socket.EVENT_ERROR
            val str = it.map { any -> return@map any.toString() }
            Logger.e("连接出现问题$str")

        }?.on(Socket.EVENT_CONNECT_TIMEOUT) {
            socketState = Socket.EVENT_CONNECT_TIMEOUT
            Logger.e("连接超时")

        }?.on(Socket.EVENT_MESSAGE) {
            socketState = Socket.EVENT_MESSAGE
            val string = GsonUtil.getInstance().toJson(it[0])
            val json = GsonUtil.getInstance().fromJson(string, MsgBean::class.java)

            addText(json.nameValuePairs)

        }?.on("receiveMsg") {
            val string = GsonUtil.getInstance().toJson(it[0])
            val json = GsonUtil.getInstance().fromJson(string, MsgBean::class.java)

            addText(json.nameValuePairs)
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

    private fun sendMessageInSocketIo(msg: String) {
        msgObj.put("client", "Android端")
        msgObj.put("msg", msg)
        mSocketIO?.emit("sendMsg", msgObj)
        val nameValuePairs = NameValuePairs("Android端", msg)
        addText(nameValuePairs)
    }

    @SuppressLint("SetTextI18n")
    private fun addText(name: NameValuePairs) {
        val str = StringBuilder()
        str.append(name.client + " ")
        str.append(name.msg)
        runOnUiThread {
            serverReturn.text = serverReturn.text.toString() + "\n$str"
        }
    }
}
package com.ztn.app.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by 冒险者ztn on 2019/3/11.
 * 线程池功能
 */
object ZtnExecutors {
    val diskIO = Executors.newSingleThreadExecutor()
    val networkIO = Executors.newFixedThreadPool(3)
    val mainThread = MainThreadExecutor()

    class MainThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable?) {
            handler.post(command)
        }
    }
}
package com.ztn.app.util

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.ztn.app.BuildConfig

/**
 * Created by 冒险者ztn on 2019/3/21.
 */
class LogUtils {
    companion object {
        fun init() {
            init("")
        }

        fun init(tag: String) {
            Logger.clearLogAdapters()
            val prettyFormatStrategy =
                if ("" == tag) PrettyFormatStrategy.newBuilder().build() else PrettyFormatStrategy.newBuilder().tag(tag).build()
            Logger.addLogAdapter(object : AndroidLogAdapter(prettyFormatStrategy) {
                override fun isLoggable(priority: Int, tag: String?): Boolean {
                    return BuildConfig.DEBUG
                }
            })
        }
    }
}
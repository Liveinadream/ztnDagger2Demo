package com.ztn.network

import com.google.gson.Gson

/**
 * Created by 冒险者ztn on 2018/10/18.
 * Gson类
 */

class GsonUtil {

    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = Gson()
    }

}
package com.ztn.network

import org.json.JSONObject

/**
 * Created by 冒险者ztn on 2018/11/30.
 */
val String.json: String
    get() = JSONObject.quote(this)
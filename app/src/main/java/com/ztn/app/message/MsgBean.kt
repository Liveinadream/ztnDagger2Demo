package com.ztn.app.message

/**
 * Created by 冒险者ztn on 2019/3/25.
 */

data class MsgBean(val nameValuePairs: NameValuePairs)

data class NameValuePairs(val client: String, val msg: String)
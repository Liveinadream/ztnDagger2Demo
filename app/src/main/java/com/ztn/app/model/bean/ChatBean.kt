package com.ztn.app.model.bean

import java.util.*

/**
 * Created by 冒险者ztn on 2019/3/13.
 * 聊天信息
 * @param message 聊天内容
 * @param mesId 聊天id
 * @param time 聊天事件
 */

data class ChatBean(val message: Message, val mesId: Int, val time: Date)

data class Message(val msg: String)
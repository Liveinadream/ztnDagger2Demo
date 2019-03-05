package com.ztn.app.adapter

/**
 * Created by 冒险者ztn on 2019/3/4.
 * 介绍 todo
 */
enum class FileAdapterEnum {
    INSTANCE;

    fun getNum() = adapterNum

    constructor() {
        val a = 5
        adapterNum++
    }
}


var adapterNum = 0
package com.ztn.app.model.bean

/**
 * Created by 冒险者ztn on 2019/3/5.
 * 文件类包含的信息
 */
data class FileBean(
    val name: String,
    val isFileDir: Boolean = false,
    val path: String,
    val show: String,
    var selected: Boolean = false
)
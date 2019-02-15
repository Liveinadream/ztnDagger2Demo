package com.ztn.app.util

import android.support.design.widget.Snackbar
import android.view.View

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
object SnackbarUtil {

    fun show(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
    }

    fun showShort(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }
}

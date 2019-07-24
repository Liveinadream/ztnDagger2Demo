package com.ztn.app.ui.view

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.MotionEvent
import com.ztn.app.R
import com.ztn.app.base.SimpleActivity
import com.ztn.common.ToastHelper
import com.ztn.common.utils.invisible
import com.ztn.common.utils.visible
import kotlinx.android.synthetic.main.activity_doodling.*
import kotlinx.android.synthetic.main.activity_doodling.doodlingView

/**
 * Created by 冒险者ztn on 2019-06-07.
 */
class DoodlingActivity : SimpleActivity() {


    companion object {
        fun startWithNothing(context: Context) {
            context.startActivity(Intent(context, DoodlingActivity::class.java))
        }
    }

    private var onDrawLine = true

    override fun getLayout(): Int {
        return R.layout.activity_doodling
    }

    override fun initEventAndData() {

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.dir)
        doodlingView.setBitmap(bitmap)

        drawLine.setOnClickListener {
            onDrawLine = true
        }

        drawText.setOnClickListener {
            onDrawLine = false
        }

        doodlingView.setOnTouchListener { v, event ->
            event?.apply {
                when (action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (onDrawLine) {
                            doodlingView.startDrawLine(x, y)
                        } else {
                            editText.visible()
                            editText.requestFocus()
                            doodlingView.statDrawText(x, y)
                        }
                        return@setOnTouchListener true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (onDrawLine) {
                            doodlingView.drawPath(x, y)
                            return@setOnTouchListener true
                        } else {
                            return@setOnTouchListener false
                        }
                    }
                    else -> {
                        return@setOnTouchListener false
                    }
                }
            }
            return@setOnTouchListener false
        }

        editText.setOnFocusChangeListener { v, hasFocus ->

            ToastHelper.showToast("编辑框焦点发生了变化")
            val string = editText.text.toString()
            if (string.isNotEmpty()) {
                if (hasFocus) {
                    doodlingView.drawText(string)
                } else {
                    doodlingView.drawText(string)
                    editText.setText("")
                }
            }


        }
    }

    override fun onBackPressedSupport() {
        if (editText.hasFocus()) {
            editText.clearFocus()
            editText.invisible()
        } else {
            super.onBackPressedSupport()
        }

    }
}
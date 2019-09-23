package com.ztn.app.ui.view

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.InputMethodManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ztn.app.R
import com.ztn.app.base.SimpleActivity
import com.ztn.commom.view.DoodlingView
import com.ztn.commom.view.DoodlingView.Companion.DRAW_LINE
import com.ztn.commom.view.DoodlingView.Companion.DRAW_TEXT
import com.ztn.commom.view.DoodlingView.Companion.NOT_ON_DRAW
import com.ztn.common.utils.gone
import com.ztn.common.utils.invisible
import com.ztn.common.utils.visible
import kotlinx.android.synthetic.main.activity_doodling.*
import java.util.*

/**
 * Created by 冒险者ztn on 2019-06-07.
 */
class DoodlingActivity : SimpleActivity() {

    lateinit var imm: InputMethodManager

    companion object {
        fun startWithNothing(context: Context) {
            context.startActivity(Intent(context, DoodlingActivity::class.java))
        }
    }

    private var adapter: BaseQuickAdapter<Int, BaseViewHolder>? = null


    override fun getLayout(): Int {
        return R.layout.activity_doodling
    }

    override fun initEventAndData() {

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.dir)
        doodlingView.setBitmap(bitmap)


        drawLine.setOnClickListener {
            doodlingView.apply {
                setDrawState(DRAW_LINE)
                if (getDrawState() == DRAW_LINE) {
                    (drawLine.background as GradientDrawable).setColor(
                        ContextCompat.getColor(
                            drawText.context,
                            doodlingView.drawColor
                        )
                    )
                    (drawText.background as GradientDrawable).setColor(
                        ContextCompat.getColor(
                            drawLine.context,
                            R.color.transparent_33000000
                        )
                    )
                } else {
                    (drawLine.background as GradientDrawable).setColor(
                        ContextCompat.getColor(
                            drawLine.context,
                            R.color.transparent_33000000
                        )
                    )
                    (drawText.background as GradientDrawable).setColor(
                        ContextCompat.getColor(
                            drawText.context,
                            R.color.transparent_33000000
                        )
                    )
                }
            }
        }

        drawText.setOnClickListener {
            doodlingView?.apply {
                setDrawState(DRAW_TEXT)
                if (getDrawState() == DRAW_TEXT) {
                    (drawText.background as GradientDrawable).setColor(
                        ContextCompat.getColor(
                            drawText.context,
                            doodlingView.drawColor
                        )
                    )
                    (drawLine.background as GradientDrawable).setColor(
                        ContextCompat.getColor(
                            drawLine.context,
                            R.color.transparent_33000000
                        )
                    )
                } else {
                    (drawText.background as GradientDrawable).setColor(
                        ContextCompat.getColor(
                            drawLine.context,
                            R.color.transparent_33000000
                        )
                    )
                    (drawLine.background as GradientDrawable).setColor(
                        ContextCompat.getColor(
                            drawText.context,
                            R.color.transparent_33000000
                        )
                    )
                }
            }
        }

        editText.setOnFocusChangeListener { _, hasFocus ->

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

        revokeDoodling.setOnClickListener {
            doodlingView.delPath()
        }

        doodlingView.addPathNumChangeListener(object : DoodlingView.DoodlingPathNamListener {
            override fun pathNumChange(num: Int) {
                if (num == 0) {
                    revokeDoodling.gone()
                } else {
                    revokeDoodling.visible()
                }
            }

        })

        doodlingView.addDrawStateChangeListener(object :
            DoodlingView.DoodlingOnActionDownStateListener {
            override fun stateOnActionDown(state: Int) {
                if (state == DRAW_TEXT) {
                    editText.visible()
                    editText.requestFocus()
                    imm.showSoftInput(editText, 0)

                } else {
                    editText.gone()
                    editText.clearFocus()
                    editText.setText("")
                    imm.hideSoftInputFromWindow(editText.windowToken, 0)
                }
            }

        })

        val drawColors = ArrayList<Int>(8)
        drawColors.add(R.color.white_6f)
        drawColors.add(R.color.red_ff3232)
        drawColors.add(R.color.orange_f5a623)
        drawColors.add(R.color.yellow_f8e71c)
        drawColors.add(R.color.green_7ed321)
        drawColors.add(R.color.blue_4a90e2)
        drawColors.add(R.color.purple_9013fe)
        drawColors.add(R.color.black_000)

        adapter = object :
            BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_doodling_color, drawColors) {
            override fun convert(helper: BaseViewHolder?, item: Int) {
                helper?.apply {
                    setBackgroundColor(
                        R.id.colorView,
                        ContextCompat.getColor(this@DoodlingActivity, item)
                    )
                }
            }

        }

        //点击后设置绘制颜色
        (adapter as BaseQuickAdapter<Int, BaseViewHolder>).onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { _, _, position ->
                doodlingView.drawColor = drawColors[position]
                when (doodlingView.getDrawState()) {
                    NOT_ON_DRAW -> {
                        (drawText.background as GradientDrawable).setColor(
                            ContextCompat.getColor(
                                this,
                                R.color.transparent_33000000
                            )
                        )
                        (drawLine.background as GradientDrawable).setColor(
                            ContextCompat.getColor(
                                this,
                                R.color.transparent_33000000
                            )
                        )
                    }

                    DRAW_LINE -> {
                        (drawText.background as GradientDrawable).setColor(
                            ContextCompat.getColor(
                                this,
                                R.color.transparent_33000000
                            )
                        )
                        (drawLine.background as GradientDrawable).setColor(
                            ContextCompat.getColor(
                                this,
                                doodlingView.drawColor
                            )
                        )

                    }

                    DRAW_TEXT -> {
                        (drawText.background as GradientDrawable).setColor(
                            ContextCompat.getColor(
                                this,
                                doodlingView.drawColor
                            )
                        )
                        (drawLine.background as GradientDrawable).setColor(
                            ContextCompat.getColor(
                                this,
                                R.color.transparent_33000000
                            )
                        )

                    }


                }
            }

        colorRv.layoutManager = LinearLayoutManager(this)
        colorRv.adapter = adapter
    }

    override fun onBackPressedSupport() {
        if (editText.hasFocus()) {
            editText.clearFocus()
            editText.invisible()
        } else {
            doodlingView.destroy()
            super.onBackPressedSupport()
        }
    }
}



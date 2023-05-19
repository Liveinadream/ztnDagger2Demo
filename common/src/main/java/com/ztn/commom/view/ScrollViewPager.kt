package com.ztn.commom.view

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by 冒险者ztn on 2019/3/13.
 * 可以设定能否滑动的 viewpager
 */
class ScrollViewPager : ViewPager {
    private var noScroll = true //true 代表不能滑动 //false 代表能滑动

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    fun setNoScroll(noScroll: Boolean) {
        this.noScroll = noScroll
    }

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        return if (noScroll)
            false
        else
            super.onTouchEvent(arg0)
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return if (noScroll)
            false
        else
            super.onInterceptTouchEvent(arg0)
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, false)//表示切换的时候，不需要切换时间。
    }

}

package com.ztn.app.ui.album

import com.ztn.app.base.BaseView
import com.ztn.app.base.IPresenter

/**
 * Created by 冒险者ztn on 2020/3/12.
 * 介绍 todo
 */
interface AlbumContract {
    interface View :BaseView{
        //把数据刷新到界面上
        fun refresh();
    }

    interface Presenter : IPresenter<View> {

        fun loadMediaStoreData()
    }



}
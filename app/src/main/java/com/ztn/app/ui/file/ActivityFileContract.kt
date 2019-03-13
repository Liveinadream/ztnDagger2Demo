package com.ztn.app.ui.file

import com.ztn.app.base.BaseView
import com.ztn.app.base.IPresenter
import java.io.File

/**
 * Created by 冒险者ztn on 2019/3/4.
 */
interface ActivityFileContract {

    interface View : BaseView {

        fun showPath()

        fun clickItem(path: String)

    }

    interface Present : IPresenter<View> {

    }
}
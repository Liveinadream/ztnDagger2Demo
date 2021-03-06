package com.ztn.app.fragment.file

import android.content.Intent
import com.ztn.app.base.BaseView
import com.ztn.app.base.IPresenter
import com.ztn.network.bean.FileBean
import java.io.File

/**
 * Created by 冒险者ztn on 2019/3/4.
 */
interface FragmentFileContract {

    interface View : BaseView {
        fun showList(list: MutableList<FileBean>)

        fun showPath(usePath: String)

        fun openInActivity(intent: Intent)
    }

    interface Present : IPresenter<View> {

        fun clickItem(path: String)

        fun backup(file: File)

        fun openFile(file: File)

    }
}
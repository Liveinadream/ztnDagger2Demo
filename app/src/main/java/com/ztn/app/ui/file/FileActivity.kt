package com.ztn.app.ui.file

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.annotation.IdRes
import android.view.View
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ztn.app.R
import com.ztn.app.base.BaseActivity
import com.ztn.app.fragment.file.FileFragment
import com.ztn.common.ToastHelper
import com.ztn.common.utils.gone
import com.ztn.common.utils.visible


/**
 * Created by 冒险者ztn on 2019/3/4.
 * 文件列表界面
 */
class FileActivity : BaseActivity<ActivityFilePresenter>(), ActivityFileContract.View {

    companion object {
        fun startWithNothing(context: Context) {
            context.startActivity(Intent(context, FileActivity::class.java))
        }
    }

    private lateinit var path:TextView
    private lateinit var seeTheSelected: FloatingActionButton

    //当前的路径
    private lateinit var usePath: String

    //点击的路径以及在那个位置
    private lateinit var clickPath: ArrayList<Pair<String, Int>>

    private lateinit var fileFragmentList: ArrayList<FileFragment>

    override fun getLayout(): Int {
        return R.layout.activity_files
    }

    override fun loading() {
    }

    override fun dismissLoading() {
    }

    override fun initInject() {
        getActivityComponent().inject(this)
    }


    override fun initEventAndData() {
        clickPath = ArrayList()
        activityTitle.text = "浏览文件"
    }

    //fragment 点击了 item 调用的方法
    override fun clickItem(path: String) {
        usePath = path
        showPath()
        addFragment(FileFragment.getInstance(usePath))
    }

    override fun onViewCreated() {
        super.onViewCreated()
        path = findViewById(R.id.path)
        seeTheSelected = findViewById(R.id.seeTheSelected)
        mPresenter.attachView(this)
        usePath = Environment.getExternalStorageDirectory().path
        path.text = usePath
        path.setOnClickListener {
            if (usePath == Environment.getExternalStorageDirectory().path) {
                ToastHelper.showToast("已经是最上级了")
            } else {
                goBack()
            }
        }

        seeTheSelected.setOnClickListener {
            val data = fileFragmentList[fileFragmentList.size - 1].data

            ToastHelper.showToast(data.filter {
                it.selected
            }.map {
                return@map it.name
            }.toString())
        }
        fileFragmentList = ArrayList()
        addFragment(FileFragment.getInstance(usePath))
    }

    private fun addFragment(fragment: FileFragment) {
        //隐藏当前 fragment
        if (fileFragmentList.size > 0)
            supportFragmentManager.beginTransaction().hide(fileFragmentList[fileFragmentList.size - 1]).commit()

        fileFragmentList.add(fragment)
        supportFragmentManager.beginTransaction().add(R.id.content, fragment).commit()
        supportFragmentManager.beginTransaction().show(fragment).commit()
        seeTheSelected.gone()
    }

    private fun delFragment() {
        val fragment = fileFragmentList[fileFragmentList.size - 1]
        fileFragmentList.removeAt(fileFragmentList.size - 1)
        supportFragmentManager.beginTransaction().detach(fragment).commit()

        //显示隐藏的 fragment
        supportFragmentManager.beginTransaction().show(fileFragmentList[fileFragmentList.size - 1]).commit()

        if (fileFragmentList[fileFragmentList.size - 1].data.any { it.selected }) {
            seeTheSelected.visible()
        }
    }

    //通过id找到某个view
    fun getViewById(@IdRes id: Int): View {
        return findViewById(id)
    }

    override fun onBackPressedSupport() {
        if (usePath == Environment.getExternalStorageDirectory().path) {
            finish()
        } else {
            goBack()
        }

    }

    private fun goBack() {
        delFragment()
        usePath = fileFragmentList[fileFragmentList.size - 1].getPath()
        showPath()
    }

    override fun showPath() {
        path.text = usePath
    }

}
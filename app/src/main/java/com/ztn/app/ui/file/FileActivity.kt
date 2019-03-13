package com.ztn.app.ui.file

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ztn.app.R
import com.ztn.app.base.BaseActivity
import com.ztn.app.fragment.file.FileFragment
import com.ztn.app.model.bean.FileBean
import com.ztn.common.ToastHelper
import kotlinx.android.synthetic.main.activity_files.*


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

    private var adapter: BaseQuickAdapter<FileBean, BaseViewHolder>? = null

    //当前的路径
    private lateinit var usePath: String

    //点击的路径以及在那个位置
    private lateinit var clickPath: ArrayList<Pair<String, Int>>

    private lateinit var fileFragmentList: ArrayList<Fragment>


    override fun getLayout(): Int {
        return R.layout.activity_files
    }

    override fun loading() {
    }

    override fun dismissLoading() {
    }

    override fun showPath(usePath: String) {
        path.text = usePath
        this.usePath = usePath
    }


    override fun initInject() {
        getActivityComponent().inject(this)
    }


    override fun initEventAndData() {
        clickPath = ArrayList()
    }

    //fragment 点击了 item 调用的方法
    override fun clickItem(path: String) {
        usePath = path
        fileFragmentList.add(FileFragment.getInstance(path))
        viewPager.adapter?.notifyDataSetChanged()
        viewPager.setCurrentItem(fileFragmentList.size - 1, false)
    }

    override fun onViewCreated() {
        super.onViewCreated()
        mPresenter.attachView(this)
        usePath = Environment.getExternalStorageDirectory().path
        path.setOnClickListener {
            if (usePath == Environment.getExternalStorageDirectory().path) {
                ToastHelper.showToast("已经是最上级了")
            } else {
                fileFragmentList.removeAt(fileFragmentList.size - 1)
                adapter?.notifyItemChanged(fileFragmentList.size - 1)
                viewPager.setCurrentItem(fileFragmentList.size - 1, false)

            }
        }

        seeTheSelected.setOnClickListener {
            adapter?.apply {
                ToastHelper.showToast(data.filter {
                    it.selected
                }.map {
                    return@map it.name
                }.toString())
            }


        }
        fileFragmentList = ArrayList()
        fileFragmentList.add(FileFragment.getInstance(usePath))
        viewPager.adapter = FileFragmentAdapter(fileFragmentList, supportFragmentManager)
        viewPager.setNoScroll(true)
    }

    //通过id找到某个view
    fun getViewById(@IdRes id: Int): View {
        return findViewById(id)
    }

    override fun onBackPressedSupport() {
        if (usePath == Environment.getExternalStorageDirectory().path) {
            finish()
        } else {
            fileFragmentList.removeAt(fileFragmentList.size - 1)
            viewPager.adapter?.notifyDataSetChanged()
            viewPager.setCurrentItem(fileFragmentList.size - 1, false)
            usePath = (fileFragmentList[fileFragmentList.size - 1] as FileFragment).getPath()
        }

    }

    class FileFragmentAdapter(private val fragmentList: ArrayList<Fragment>, fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

    }

}
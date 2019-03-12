package com.ztn.app.ui.file

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ztn.app.R
import com.ztn.app.base.BaseActivity
import com.ztn.app.base.contract.ActivityFileContract
import com.ztn.app.fragment.FileFragment
import com.ztn.app.model.bean.FileBean
import com.ztn.app.presenter.ActivityFilePresenter
import com.ztn.common.ToastHelper
import com.ztn.common.utils.gone
import com.ztn.common.utils.visible
import kotlinx.android.synthetic.main.activity_files.*
import java.io.File


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

    //选中的文件数量
    private var selectNum = 0

    private lateinit var fileFragmentList: ArrayList<Fragment>


    override fun getLayout(): Int {
        return R.layout.activity_files
    }

    override fun loading() {
    }

    override fun dismissLoading() {
    }


    override fun showList(list: MutableList<FileBean>) {
        selectNum = 0

        if (adapter == null) {
            adapter = object : BaseQuickAdapter<FileBean, BaseViewHolder>(R.layout.item_flle, list) {
                override fun convert(helper: BaseViewHolder, item: FileBean) {
                    helper.apply {
                        setText(R.id.name, item.name)
                        setText(R.id.content, item.show)

                        //建立监听
                        (getView(R.id.parent) as ConstraintLayout).setOnClickListener {
                            if (item.isFileDir) {
                                mPresenter.clickItem(item.path)
                                clickPath.add(Pair(item.path, layoutPosition))
                            } else {
                                mPresenter.openFile(File(item.path))
                            }
                        }

                        setOnCheckedChangeListener(R.id.selected) { _, isChecked ->
                            item.selected = isChecked

                            if (isChecked) {
                                selectNum++
                                seeTheSelected.visible()
                            } else {
                                selectNum--
                            }

                            if (selectNum == 0) {
                                seeTheSelected.gone()
                            }
                        }

                        //判断类型
                        if (item.isFileDir) {
                            setImageResource(R.id.headImg, R.drawable.dir)
                        } else {
                            setImageResource(R.id.headImg, R.drawable.file)
                        }

                        //判断是否选中
                        if (item.selected) {
                            setChecked(R.id.selected, true)
                        } else {
                            setChecked(R.id.selected, false)
                        }
                    }


                }

            }

            fileList.hasFixedSize()
            fileList.setItemViewCacheSize(20)
            adapter?.emptyView = View.inflate(this, R.layout.no_data, LinearLayout(this))
            fileList.layoutManager = LinearLayoutManager(this)
            fileList.adapter = adapter

        } else {
            adapter?.setNewData(list)
        }

        if (clickPath.size == 0) {
//            adapter.
        }
    }

    override fun showPath(usePath: String) {
        path.text = usePath
        this.usePath = usePath
    }

    override fun openInActivity(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            ToastHelper.showToast("没有能打开的界面")
        }
    }

    override fun initInject() {
        getActivityComponent().inject(this)
    }


    override fun initEventAndData() {
        clickPath = ArrayList()
    }

    override fun onViewCreated() {
        super.onViewCreated()
        mPresenter.attachView(this)
        usePath = Environment.getExternalStorageDirectory().path
//        mPresenter.clickItem(usePath)
//        path.setOnClickListener {
//            if (usePath == Environment.getExternalStorageDirectory().path) {
//                ToastHelper.showToast("已经是最上级了")
//            } else {
//                mPresenter.backup(File(usePath))
//            }
//        }

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


    }

    override fun onBackPressedSupport() {
        if (usePath == Environment.getExternalStorageDirectory().path) {
            finish()
        } else {
            mPresenter.backup(File(usePath))
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
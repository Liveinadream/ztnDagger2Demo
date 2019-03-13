package com.ztn.app.fragment.file

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ztn.app.R
import com.ztn.app.base.BaseFragment
import com.ztn.network.bean.FileBean
import com.ztn.app.ui.file.FileActivity
import com.ztn.common.ToastHelper
import com.ztn.common.utils.gone
import com.ztn.common.utils.visible
import kotlinx.android.synthetic.main.fragment_files.*
import java.io.File

/**
 * Created by 冒险者ztn on 2019/3/12.
 * 展示文件列表的 fragment
 */
class FileFragment : BaseFragment<FragmentFilePresenter>(), FragmentFileContract.View {

    companion object {
        const val PATH = "path"

        fun getInstance(path: String): FileFragment {
            val fragment = FileFragment()
            val bundle = Bundle()
            bundle.putString(PATH, path)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var adapter: BaseQuickAdapter<FileBean, BaseViewHolder>? = null
    private lateinit var fileActivity: FileActivity

    //当前的路径
    private lateinit var usePath: String

    //选中的文件数量
    private var selectNum = 0

    lateinit var data: MutableList<FileBean>


    override fun showList(list: MutableList<FileBean>) {
        selectNum = 0
        data = list
        if (adapter == null) {
            adapter = object : BaseQuickAdapter<FileBean, BaseViewHolder>(R.layout.item_flle, list) {

                override fun convert(helper: BaseViewHolder, item: FileBean) {
                    helper.apply {
                        setText(R.id.name, item.name)
                        setText(R.id.content, item.show)

                        //建立监听
                        (getView(R.id.parent) as ConstraintLayout).setOnClickListener {
                            if (item.isFileDir) {
                                fileActivity.clickItem(item.path)
                            } else {
                                mPresenter.openFile(File(item.path))
                            }
                        }

                        setOnCheckedChangeListener(R.id.selected) { _, isChecked ->
                            item.selected = isChecked


                            if (isChecked) {
                                selectNum++
                                fileActivity.getViewById(R.id.seeTheSelected).visible()
                            } else {
                                selectNum--
                            }

                            if (selectNum == 0) {
                                fileActivity.getViewById(R.id.seeTheSelected).gone()
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
            adapter?.emptyView = View.inflate(this.context, R.layout.no_data, LinearLayout(this.context))
            fileList.layoutManager = LinearLayoutManager(this.context)
            fileList.adapter = adapter
        } else {
            adapter?.setNewData(list)
        }
    }

    override fun showPath(usePath: String) {
        (fileActivity.getViewById(R.id.path) as TextView).text = usePath
    }

    override fun openInActivity(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            ToastHelper.showToast("没有能打开的界面")
        }
    }

    override fun loading() {
    }

    override fun dismissLoading() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_files
    }

    override fun initInject() {
        getFragmentComponent().inject(this)
    }

    fun getPath(): String {
        return usePath
    }

    override fun initView() {
    }

    override fun lazyLoad() {
        mPresenter = FragmentFilePresenter()
        fileActivity = activity as FileActivity
        mPresenter.attachView(this)
        usePath = arguments?.getString(PATH)!!
        mPresenter.clickItem(usePath)
    }


}
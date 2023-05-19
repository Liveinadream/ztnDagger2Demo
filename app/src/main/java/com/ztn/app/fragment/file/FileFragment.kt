package com.ztn.app.fragment.file

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ztn.app.R
import com.ztn.app.base.BaseFragment
import com.ztn.network.bean.FileBean
import com.ztn.app.ui.file.FileActivity
import com.ztn.common.ToastHelper
import com.ztn.common.utils.gone
import com.ztn.common.utils.visible
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
    private lateinit var fileList: RecyclerView
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
            adapter =
                object : BaseQuickAdapter<FileBean, BaseViewHolder>(R.layout.item_flle, list) {

                    override fun convert(holder: BaseViewHolder, item: FileBean) {
                        holder.apply {
                            setText(R.id.name, item.name)
                            setText(R.id.content, item.show)
                            getView<TextView>(R.id.name).isSelected = true
                            //建立监听
                            (getView(R.id.parent) as ConstraintLayout).setOnClickListener {
                                if (item.isFileDir) {
                                    fileActivity.clickItem(item.path)
                                } else {
                                    mPresenter.openFile(File(item.path))
                                }
                            }

                            getView<AppCompatCheckBox>(R.id.selected).setOnCheckedChangeListener { _, isChecked ->
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
                            getView<AppCompatCheckBox>(R.id.selected).isChecked = item.selected
                        }


                    }

                }

            fileList.hasFixedSize()
            fileList.setItemViewCacheSize(20)
            adapter?.setEmptyView(View.inflate(context, R.layout.no_data, LinearLayout(context)))
            fileList.layoutManager =
                LinearLayoutManager(context)
            fileList.adapter = adapter
        } else {
            adapter?.setNewInstance(list)
        }
    }

    override fun showPath(usePath: String) {
        (fileActivity.getViewById(R.id.path) as TextView).text = usePath
    }

    override fun openInActivity(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            ToastHelper.showToast("没有能打开的文件的应用")
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

    override fun lazyLoad() {
        mPresenter = FragmentFilePresenter()
        fileActivity = activity as FileActivity
        mPresenter.attachView(this)
        usePath = arguments?.getString(PATH)!!
        mPresenter.clickItem(usePath)
        fileList = fileActivity.findViewById(R.id.fileList)
    }


}
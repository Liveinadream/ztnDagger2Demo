package com.ztn.app.fragment

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ztn.app.R
import com.ztn.app.base.BaseFragment
import com.ztn.app.base.contract.FragmentFileContract
import com.ztn.app.model.bean.FileBean
import com.ztn.app.presenter.FragmentFilePresenter
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

    //当前的路径
    private lateinit var usePath: String

    //点击的路径以及在那个位置
    private lateinit var clickPath: ArrayList<Pair<String, Int>>

    //选中的文件数量
    private var selectNum = 0


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

//                            if (isChecked) {
//                                selectNum++
//                                seeTheSelected.visible()
//                            } else {
//                                selectNum--
//                            }
//
//                            if (selectNum == 0) {
//                                seeTheSelected.gone()
//                            }
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

        if (clickPath.size == 0) {
//            adapter.
        }
    }


    override fun showPath(usePath: String) {
    }

    override fun openInActivity(intent: Intent) {
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

    override fun initView() {
    }

    override fun lazyLoad() {
        mPresenter = FragmentFilePresenter()
        mPresenter.attachView(this)
        usePath = arguments?.getString(PATH)!!
        mPresenter.clickItem(usePath)
    }


}
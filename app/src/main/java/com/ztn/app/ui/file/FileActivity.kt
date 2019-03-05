package com.ztn.app.ui.file

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ztn.app.R
import com.ztn.app.base.BaseActivity
import com.ztn.app.base.contract.FileContract
import com.ztn.app.model.bean.FileBean
import com.ztn.app.presenter.FilePresent
import com.ztn.common.ToastHelper
import kotlinx.android.synthetic.main.activity_files.*
import java.io.File


/**
 * Created by 冒险者ztn on 2019/3/4.
 * 文件列表界面
 */
class FileActivity : BaseActivity<FilePresent>(), FileContract.View {


    companion object {
        fun startWithNothing(context: Context) {
            context.startActivity(Intent(context, FileActivity::class.java))
        }
    }

    private var adapter: BaseQuickAdapter<FileBean, BaseViewHolder>? = null
    private lateinit var usePath: String


    override fun getLayout(): Int {
        return R.layout.activity_files
    }

    override fun loading() {
    }

    override fun dismissLoading() {
    }


    override fun showList(list: MutableList<FileBean>) {


        if (adapter == null) {
            adapter = object : BaseQuickAdapter<FileBean, BaseViewHolder>(R.layout.item_flle, list) {
                override fun convert(helper: BaseViewHolder, item: FileBean) {

                    helper.apply {
                        setText(R.id.name, item.name)
                        setText(R.id.content, item.show)

                        (getView(R.id.parent) as ConstraintLayout).setOnClickListener {
                            if (item.isFileDir) {
                                mPresenter.clickItem(item.path)
                            } else {
                                ToastHelper.showToast("暂不支持打开文件")
                            }
                        }

                        if (item.isFileDir) {
                            setImageResource(R.id.headImg, R.drawable.dir)
                        } else {
                            setImageResource(R.id.headImg, R.drawable.file)
                        }
                    }


                }

            }

            fileList.hasFixedSize()
            fileList.setItemViewCacheSize(20)
//            adapter?.emptyView = View.inflate(this, R.layout.no_data, fileList,false)
            fileList.layoutManager = LinearLayoutManager(this)
            fileList.adapter = adapter

        } else {
            adapter?.setNewData(list)
        }
    }

    override fun showPath(usePath: String) {
        path.text = usePath
        this.usePath = usePath
    }

    override fun initInject() {
        getActivityComponent().inject(this)
    }


    override fun initEventAndData() {
    }

    override fun onViewCreated() {
        super.onViewCreated()
        mPresenter.attachView(this)
        usePath = Environment.getExternalStorageDirectory().path
        mPresenter.clickItem(Environment.getExternalStorageDirectory().path)
        path.setOnClickListener {
            if (usePath == Environment.getExternalStorageDirectory().path) {
                val toast = Toast.makeText(this, null, Toast.LENGTH_SHORT)
                toast.setText("已经是最上级了")
                toast.show()
            } else {
                mPresenter.backup(File(usePath))
            }
        }

    }


}
package com.ztn.app.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ztn.app.R
import com.ztn.app.base.SimpleActivity
import kotlinx.android.synthetic.main.activity_installed_apk.*
import com.ztn.common.ToastHelper
import com.ztn.common.utils.show


/**
 * Created by 冒险者ztn on 2019/2/18.
 * 已安装的 apk 界面
 */
class InstalledActivity : SimpleActivity() {

    companion object {
        fun startWithNothing(context: Context) {
            context.startActivity(Intent(context, InstalledActivity::class.java))
        }
    }

    private lateinit var myPackageInfo: MutableList<PackageInfo>
    private lateinit var adapter: BaseQuickAdapter<PackageInfo, BaseViewHolder>
    private lateinit var drawableList: ArrayList<Drawable>

    override fun getLayout(): Int {
        return R.layout.activity_installed_apk
    }

    override fun initEventAndData() {
        activityTitle.text = "已安装应用"

        Thread {
            val packageInfo = packageManager.getInstalledPackages(0)
            myPackageInfo = ArrayList(packageInfo.size)

            packageInfo.forEach {
                if ((it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                    myPackageInfo.add(it)
                }
            }
            drawableList = ArrayList(myPackageInfo.size)
            myPackageInfo.forEach {
                drawableList.add(packageManager.getApplicationIcon(it.packageName))

            }

            adapter =
                object : BaseQuickAdapter<PackageInfo, BaseViewHolder>(
                    R.layout.item_installed_apk,
                    myPackageInfo
                ) {
                    override fun convert(helper: BaseViewHolder?, item: PackageInfo) {
                        helper?.apply {

                            setText(
                                R.id.name,
                                packageManager.getApplicationInfo(item.packageName, 0).loadLabel(packageManager)
                            )
                            setText(R.id.packageName, item.packageName)
                            getView<TextView>(R.id.packageName).isSelected = true
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                setText(R.id.content, "版本名称：${item.versionName} 版本号：${item.longVersionCode}")
                            } else {
                                setText(R.id.content, "版本名称：${item.versionName} 版本号：${item.versionCode}")
                            }

                            setImageDrawable(R.id.headImg, drawableList[this.adapterPosition])

                        }
                    }
                }

            showList()

        }.start()


    }

    private fun showList() {
        runOnUiThread {

            adapter.setOnItemClickListener { adapter, view, position ->

                val packageManager = packageManager
                val intent = packageManager.getLaunchIntentForPackage(myPackageInfo[position].packageName)
                if (intent == null) {
                    ToastHelper.show("未安装")
                } else {
                    startActivity(intent)
                }
            }

            installedApkList.adapter = adapter
            installedApkList.layoutManager = LinearLayoutManager(this)
            installedApkList.setHasFixedSize(true)
            installedApkList.setItemViewCacheSize(20)
        }

    }


}
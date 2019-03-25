package com.ztn.app.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ztn.app.R
import com.ztn.app.base.SimpleActivity
import kotlinx.android.synthetic.main.activity_installed_apk.*


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

    override fun getLayout(): Int {
        return R.layout.activity_installed_apk
    }

    override fun initEventAndData() {
        activityTitle.text = "已安装应用"
        myPackageInfo = ArrayList()
        val packageInfo = packageManager.getInstalledPackages(0)
        packageInfo.forEach {
            if ((it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                myPackageInfo.add(it)
            }
        }

        adapter =
            object : BaseQuickAdapter<PackageInfo, BaseViewHolder>(R.layout.item_installed_apk, myPackageInfo) {
                override fun convert(helper: BaseViewHolder?, item: PackageInfo) {
                    helper?.apply {

                        setText(
                            R.id.name,
                            packageManager.getApplicationInfo(item.packageName, 0).loadLabel(packageManager)
                        )
                        setText(R.id.packageName, item.packageName)
                        setText(R.id.content, "版本名称：${item.versionName} 版本号：${item.versionCode}")
                        setImageDrawable(R.id.headImg, packageManager.getApplicationIcon(item.packageName))

                    }
                }
            }
        installedApkList.hasFixedSize()
        installedApkList.setItemViewCacheSize(20)
        installedApkList.layoutManager = LinearLayoutManager(this)
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        installedApkList.adapter = adapter
//        method(add(1, 2), 2)
    }
//
//    private fun method(t: (Int, Int) -> Int, k: Int) {
//        t.invoke(k, k)
//    }
//
//    private fun add(a: Int, b: Int): (Int, Int) -> Int {
//        return fun(c: Int, d: Int): Int {
//            return c + d
//        }
//    }

}
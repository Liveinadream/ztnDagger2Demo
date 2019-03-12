package com.ztn.app.presenter

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.util.Log
import com.ztn.app.base.BasePresenter
import com.ztn.app.base.contract.FragmentFileContract
import com.ztn.app.model.bean.FileBean
import com.ztn.app.rx.CommonOnSubscribe
import com.ztn.common.framework.AppManager
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * Created by 冒险者ztn on 2019/3/5.
 * 介绍 todo
 */
class FragmentFilePresenter @Inject constructor() : BasePresenter<FragmentFileContract.View>(),
    FragmentFileContract.Present {


    override fun openFile(file: File) {
        //判断系统是否是7.0
        val localUrl = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {
            FileProvider.getUriForFile(AppManager.context, "${AppManager.context.packageName}.FileProvider", file)
        }
        val mime = AppManager.context.contentResolver.getType(localUrl)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(localUrl, mime)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        mRootView?.get()?.openInActivity(intent)
    }

    override fun backup(file: File) {
        clickItem(file.parent)
    }


    override fun clickItem(path: String) {

        mRootView?.get()?.showPath(path)
        compositeDisposable.add(

            Observable.create(object : CommonOnSubscribe<ArrayList<FileBean>>() {
                override fun work(e: ObservableEmitter<ArrayList<FileBean>>) {

                    val fileDir = File(path)
                    val fileList = ArrayList<FileBean>(fileDir.listFiles().size)
                    val files = ArrayList<FileBean>()
                    val dirs = ArrayList<FileBean>()



                    fileDir.listFiles().forEach {
                        if (it.isDirectory) {
                            dirs.add(FileBean(it.name, true, it.path, show = "${it.listFiles().size} 项"))
                        } else {
                            val size = it.length()

                            val show: String = when {
                                size < 1024 -> "${String.format("%.2f", size.toFloat())} bytes"

                                size < 1024 * 1024 -> "${String.format("%.2f", (size / 1024f))} KB"

                                size < 1024 * 1024 * 1024 -> "${String.format(
                                    "%.2f",
                                    (size / (1024f * 1024f))
                                )} MB"

                                else -> "${String.format("%.2f", (size / (1024f * 1024f * 1024f)))} GB"
                            }

                            files.add(FileBean(it.name, path = it.path, show = show))
                        }

                    }

                    files.sortBy {
                        it.name.toUpperCase()
                    }

                    dirs.sortBy {
                        it.name.toUpperCase()
                    }

                    fileList.addAll(dirs)
                    fileList.addAll(files)
                    e.onNext(fileList)
                    e.onComplete()

                }

            }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mRootView?.get()?.showList(it)

                }, {
                    Log.e("错误:", it.message)
                })
        )


    }

}



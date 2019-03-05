package com.ztn.app.presenter

import android.util.Log
import com.ztn.app.base.BasePresenter
import com.ztn.app.base.contract.FileContract
import com.ztn.app.model.bean.FileBean
import com.ztn.app.rx.CommonOnSubscribe
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

/**
 * Created by 冒险者ztn on 2019/3/5.
 * 介绍 todo
 */
class FilePresenter @Inject constructor() : BasePresenter<FileContract.View>(), FileContract.Present {

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
                            dirs.add(FileBean(it.name, true, it.path, show = "${it.listFiles().size}个"))
                        } else {
                            val size = it.length()

                            val show: String = when {
                                size < 1024 -> "${String.format("%.2f", size.toFloat())}bytes"

                                size < 1024 * 1024 -> "${String.format("%.2f", (size / 1024).toFloat())}KB"

                                size < 1024 * 1024 * 1024 -> "${String.format("%.2f", (size / 1024 * 1024).toFloat())}MB"

                                else -> "${String.format("%.2f", (size / (1024 * 1024 * 1024)).toFloat())}GB"
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



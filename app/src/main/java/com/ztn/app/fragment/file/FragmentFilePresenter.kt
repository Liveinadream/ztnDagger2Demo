package com.ztn.app.fragment.file

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import com.j256.simplemagic.ContentInfoUtil
import com.j256.simplemagic.ContentInfoUtilSingle
import com.ztn.app.base.BasePresenter
import com.ztn.common.framework.AppManager
import com.ztn.common.utils.log
import com.ztn.library.rx.CommonOnSubscribe
import com.ztn.network.bean.FileBean
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.net.URLConnection
import java.nio.file.Files
import java.util.*
import javax.inject.Inject


/**
 * Created by 冒险者ztn on 2019/3/5.
 */
class FragmentFilePresenter @Inject constructor() : BasePresenter<FragmentFileContract.View>(),
    FragmentFileContract.Present {


    override fun openFile(file: File) {
        //判断系统是否是7.0，文件识别功能，暂时注释掉了，
//        val localUrl = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//            Uri.fromFile(file)
//        } else {
//            FileProvider.getUriForFile(
//                AppManager.context,
//                "${AppManager.context.packageName}.FileProvider",
//                file
//            )
//        }
//        val mime = AppManager.context.contentResolver.getType(localUrl)
//        Thread {
//            var mime: String? = null
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                mime = Files.probeContentType(file.toPath())
//            }
//
//            if (TextUtils.isEmpty(mime)) {
//                mime = URLConnection.guessContentTypeFromName(file.absolutePath)
//            }
//
//            if (TextUtils.isEmpty(mime)) {
//                mime = URLConnection.guessContentTypeFromName(file.name)
//            }
//
//            if (mime == null) {
//                val url = file.path
//                val extension = MimeTypeMap.getFileExtensionFromUrl(url)
//                if (extension != null) {
//                    mime =
//                        MimeTypeMap.getSingleton().getMimeTypeFromExtension(
//                            extension.toLowerCase(
//                                Locale.getDefault()
//                            )
//                        )
//                }
//            }
//
//            if (TextUtils.isEmpty(mime)) {
//                mime = URLConnection.guessContentTypeFromStream(
//                    BufferedInputStream(
//                        FileInputStream(file)
//                    )
//                )
//            }
//
////            if (TextUtils.isEmpty(mime)) {
////                mime = try {
////                    Magic.getMagicMatch(file, true).mimeType
////                } catch (e: Exception) {
////                    e.message?.apply {
////                        log("错误:$this")
////                    }
////                    ""
////                }
////            }
//
//            if (TextUtils.isEmpty(mime)) {
//                val util = ContentInfoUtilSingle.getInstance()
//                if (file != null && file.length > 0)
//                    mime = util.findMatch(file).mimeType
//            }
//
//            if (TextUtils.isEmpty(mime) || TextUtils.equals("???", mime)) {
//                mime = AppManager.context.contentResolver.getType(localUrl)
//            }
//
//            log("识别的文件类型：$mime")
//
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.setDataAndType(localUrl, mime)
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            mRootView?.get()?.openInActivity(intent)
//        }.start()
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
                            dirs.add(
                                FileBean(
                                    it.name,
                                    true,
                                    it.path,
                                    show = "${it.listFiles().size} 项"
                                )
                            )
                        } else {
                            val size = it.length()

                            val show: String = when {
                                size < 1024 -> "${String.format("%.2f", size.toFloat())} bytes"

                                size < 1024 * 1024 -> "${String.format("%.2f", (size / 1024f))} KB"

                                size < 1024 * 1024 * 1024 -> "${String.format(
                                    "%.2f",
                                    (size / (1024f * 1024f))
                                )} MB"

                                else -> "${String.format(
                                    "%.2f",
                                    (size / (1024f * 1024f * 1024f))
                                )} GB"
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

    fun extensionName(name: String): String {
        if (name.length < 2 || name.endsWith(".")) return ""
        val index = name.lastIndexOf(".")
        return if (index < 0) "" else name.substring(index + 1).toLowerCase()
    }

}



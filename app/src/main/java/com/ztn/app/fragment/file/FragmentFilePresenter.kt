package com.ztn.app.fragment.file

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import com.j256.simplemagic.ContentInfoUtilSingle
import com.ztn.app.base.BasePresenter
import com.ztn.common.framework.AppManager
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
        val uri: Uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {
            FileProvider.getUriForFile(
                AppManager.context,
                "${AppManager.context.packageName}.FileProvider",
                file
            )
        }
        var mime: String? = AppManager.context.contentResolver.getType(uri)
        compositeDisposable.add(
            Observable.create(object : CommonOnSubscribe<String>() {
                override fun work(e: ObservableEmitter<String>) {
                    if (TextUtils.isEmpty(mime)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            mime = Files.probeContentType(file.toPath())
                        }
                    }

                    if (TextUtils.isEmpty(mime)) {
                        mime = URLConnection.guessContentTypeFromName(file.absolutePath)
                    }

                    if (TextUtils.isEmpty(mime)) {
                        mime = URLConnection.guessContentTypeFromName(file.name)
                    }

                    if (mime == null) {
                        val url = file.path
                        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
                        if (extension != null) {
                            mime = MimeTypeMap.getSingleton()
                                .getMimeTypeFromExtension(extension.lowercase())
                        }
                    }

                    if (TextUtils.isEmpty(mime)) {
                        mime = URLConnection.guessContentTypeFromStream(
                            BufferedInputStream(
                                FileInputStream(file)
                            )
                        )
                    }

                    if (TextUtils.isEmpty(mime)) {
                        val util = ContentInfoUtilSingle.getInstance()
                        if (file.length() > 0) {
                            val contentInfo = util.findMatch(file)
                            contentInfo.let {
                                mime = it.mimeType
                            }
                        }
                    }

                    if (TextUtils.isEmpty(mime) || TextUtils.equals("???", mime)) {
                        mime = AppManager.context.contentResolver.getType(uri)
                    }
                    Log.d("文件类型:", "识别的文件类型：$mime")
                    if (TextUtils.isEmpty(mime)) {
                        e.onError(Throwable("未能识别文件类型"))
                    } else {
                        e.onNext(mime!!)
                    }
                }

            }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, mime)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    requireRootView().openInActivity(intent)
                }, {
                    Log.e("错误:", it.message!!)
                })
        )
    }

    override fun backup(file: File) {
        val parent = file.parent ?: ""
        clickItem(parent)
    }


    override fun clickItem(path: String) {
        requireRootView().showPath(path)
        compositeDisposable.add(
            Observable.create(object : CommonOnSubscribe<ArrayList<FileBean>>() {
                override fun work(e: ObservableEmitter<ArrayList<FileBean>>) {

                    val fileDir = File(path)
                    val listFile = fileDir.listFiles() ?: return
                    val fileList = ArrayList<FileBean>(listFile.size)
                    val files = ArrayList<FileBean>()
                    val dirs = ArrayList<FileBean>()

                    listFile.forEach {
                        if (it.isDirectory) {
                            val itemFileList = it.listFiles() ?: emptyArray()
                            dirs.add(
                                FileBean(
                                    it.name,
                                    true,
                                    it.path,
                                    show = "${itemFileList.size} 项"
                                )
                            )
                        } else {
                            val size = it.length()

                            val show: String = when {
                                size < 1024 -> "${String.format("%.2f", size.toFloat())} bytes"

                                size < 1024 * 1024 -> "${String.format("%.2f", (size / 1024f))} KB"

                                size < 1024 * 1024 * 1024 -> "${
                                    String.format(
                                        "%.2f",
                                        (size / (1024f * 1024f))
                                    )
                                } MB"

                                else -> "${
                                    String.format(
                                        "%.2f",
                                        (size / (1024f * 1024f * 1024f))
                                    )
                                } GB"
                            }

                            files.add(FileBean(it.name, path = it.path, show = show))
                        }

                    }

                    files.sortBy {
                        it.name.lowercase()
                    }

                    dirs.sortBy {
                        it.name.uppercase()
                    }

                    fileList.addAll(dirs)
                    fileList.addAll(files)
                    e.onNext(fileList)
                    e.onComplete()
                }

            }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    requireRootView().showList(it)
                }, {
                    Log.e("错误:", it.message!!)
                })
        )
    }
}



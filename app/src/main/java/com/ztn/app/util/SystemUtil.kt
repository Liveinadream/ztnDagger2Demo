package com.ztn.app.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.view.View
import com.ztn.app.Constants
import com.ztn.app.base.BaseApplication
import com.ztn.common.ToastHelper
import java.io.*

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
object SystemUtil {

    /**
     * 检查WIFI是否连接
     */
    val isWifiConnected: Boolean
        get() {

            val connectivityManager =
                BaseApplication.instance.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifiInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            return wifiInfo != null
        }
    /**
     * 检查手机网络(4G/3G/2G)是否连接
     */
    val isMobileNetworkConnected: Boolean
        get() {
            val connectivityManager =
                BaseApplication.instance.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mobileNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            return mobileNetworkInfo != null
        }
    /**
     * 检查是否有可用网络
     */
    val isNetworkConnected: Boolean
        get() {
            val connectivityManager =
                BaseApplication.instance.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null
        }

    /**
     * 保存文字到剪贴板
     * @param context
     * @param text
     */
    fun copyToClipBoard(context: Context, text: String) {
        val clipData = ClipData.newPlainText("url", text)
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        manager.primaryClip = clipData
        ToastHelper.showToast("已复制到剪贴板")
    }

    /**
     * 保存图片到本地
     * @param context
     * @param url
     * @param bitmap
     */
    fun saveBitmapToFile(context: Context, url: String, bitmap: Bitmap, container: View, isShare: Boolean): Uri {
        val fileName = url.substring(url.lastIndexOf("/"), url.lastIndexOf(".")) + ".png"
        val fileDir = File(Constants.PATH_SDCARD)
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }
        val imageFile = File(fileDir, fileName)
        var uri = Uri.fromFile(imageFile)
        if (isShare && imageFile.exists()) {
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(
                    context.applicationContext,
                    Constants.FILE_PROVIDER_AUTHORITY, imageFile
                )
            }
            return uri
        }
        try {
            val fos = FileOutputStream(imageFile)
            val isCompress = bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
            if (isCompress) {
                SnackbarUtil.showShort(container, "保存妹纸成功n(*≧▽≦*)n")
            } else {
                SnackbarUtil.showShort(container, "保存妹纸失败ヽ(≧Д≦)ノ")
            }
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
            SnackbarUtil.showShort(container, "保存妹纸失败ヽ(≧Д≦)ノ")
        }

        try {
            MediaStore.Images.Media.insertImage(context.contentResolver, imageFile.absolutePath, fileName, null)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(
                context.applicationContext,
                Constants.FILE_PROVIDER_AUTHORITY, imageFile
            )
        }
        return uri
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun dp2px(dpValue: Float): Int {
        val scale = BaseApplication.instance.resources?.displayMetrics?.density
        return (dpValue * scale!! + 0.5f).toInt()
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }
}

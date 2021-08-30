package com.ztn.app.util

import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import com.ztn.app.base.BaseApplication
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * Created by 冒险者ztn on 2019/2/12.
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

package com.util

import android.content.pm.ApplicationInfo
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/10/23 13:05
 * 功能描述：
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/10/23 13:05
 * 修改内容：
 */
object ToastUtils {
    private val toast: Toast by lazy {
        Toast.makeText(AppHolder.appContext(), "", Toast.LENGTH_SHORT)
    }

    fun message(msg: String): ToastUtils {
        toast.setText(msg)
        return this
    }

    fun message(@StringRes msgRes: Int): ToastUtils {
        toast.setText(msgRes)
        return this
    }

    fun showShort() {
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    fun showLong() {
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }

    fun debugShowShort() {
        val isDebug = AppHolder.appContext().applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        if (isDebug) {
            showShort()
        }
    }

    fun debugShowLong() {
        val isDebug = AppHolder.appContext().applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        if (isDebug) {
            showLong()
        }
    }
}
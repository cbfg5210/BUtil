package com.util

import android.app.Application
import android.content.Context

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/10/22 9:00
 * 功能描述：保存 Application context 对象,提供给有需要的地方使用
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/10/22 9:00
 * 修改内容：
 */
object AppHolder {
    private lateinit var appContext: Context

    fun init(context: Context) {
        this.appContext = context.applicationContext
    }

    fun appContext(): Context {
        return appContext
    }
}
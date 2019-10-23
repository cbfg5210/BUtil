package com.butil

import android.app.Application
import com.util.AppHolder

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/10/23 13:42
 * 功能描述：
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/10/23 13:42
 * 修改内容：
 */
class RelApp :Application(){
    override fun onCreate() {
        super.onCreate()
        AppHolder.init(this)
    }
}
package com.util

import android.app.job.JobScheduler
import android.content.Context
import android.os.BatteryManager
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/10/22 9:01
 * 功能描述：系统服务对象管理,Api 17 以上
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/10/22 9:01
 * 修改内容：
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
object SysServiceHolder17 {
    //电池管理
    val batteryManager: BatteryManager by lazy {
        AppHolder.app().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    }

    //安排后台任务在特定条件下触发。省电、操作的时间并不是严格准确的
    val jobScheduler: JobScheduler by lazy {
        AppHolder.app().getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    }
}
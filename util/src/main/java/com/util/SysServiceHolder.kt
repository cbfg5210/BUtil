package com.util

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.hardware.SensorManager
import android.location.LocationManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.PowerManager
import android.os.Vibrator
import android.os.storage.StorageManager
import android.telephony.TelephonyManager
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager


/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/10/22 9:01
 * 功能描述：系统服务对象管理
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/10/22 9:01
 * 修改内容：
 */
@SuppressLint("WifiManagerLeak")
object SysServiceHolder {
    //网络连接管理
    val connManager: ConnectivityManager by lazy {
        AppHolder.appContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    //wifi 管理
    val wifiManager: WifiManager by lazy {
        AppHolder.appContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    //闹钟管理
    val alarmManager: AlarmManager by lazy {
        AppHolder.appContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    //通知管理
    val notificationManager: NotificationManager by lazy {
        AppHolder.appContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    //位置管理
    val locationManager: LocationManager by lazy {
        AppHolder.appContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    //振动传感器
    val vibrator: Vibrator by lazy {
        AppHolder.appContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    //音量管理
    val audioManager: AudioManager by lazy {
        AppHolder.appContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    //电话管理
    val telephoneManager: TelephonyManager by lazy {
        AppHolder.appContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    //输入法管理
    val inputMethodManager: InputMethodManager by lazy {
        AppHolder.appContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    //下载管理
    val downloadManager: DownloadManager by lazy {
        AppHolder.appContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    //利用此服务可以实现对系统中的应用、联系人、SMS等进行搜索
    val searchManager: SearchManager by lazy {
        AppHolder.appContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
    }

    //电源管理
    val powerManager: PowerManager by lazy {
        AppHolder.appContext().getSystemService(Context.POWER_SERVICE) as PowerManager
    }

    //获取系统内存信息以及进程信息
    val activityManager: ActivityManager by lazy {
        AppHolder.appContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

    //窗口管理
    val windowManager: WindowManager by lazy {
        AppHolder.appContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    //屏幕保护
    val keyguardManager: KeyguardManager by lazy {
        AppHolder.appContext().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    }

    //传感器管理
    val sensorManager: SensorManager by lazy {
        AppHolder.appContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    //存储管理
    val storageManager: StorageManager by lazy {
        AppHolder.appContext().getSystemService(Context.STORAGE_SERVICE) as StorageManager
    }
}
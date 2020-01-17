package com.butil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.util.KLog
import com.util.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTest.setOnClickListener { ToastUtils.message("hello").showShort() }
        tvTest.setOnLongClickListener {
            Thread { ToastUtils.message("Hello thread!").showShort() }.start()
            true
        }

        //KLog.e("abc")
        //KLog.json(KLog.E, "{\"a\":\"aa\"}")
        val map = hashMapOf("a" to "aa", "b" to "bb")
        KLog.map(KLog.E, map)
    }
}

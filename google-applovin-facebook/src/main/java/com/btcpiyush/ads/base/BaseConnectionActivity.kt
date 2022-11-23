package com.btcpiyush.ads.base

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import com.btcpiyush.ads.receiver.NetworkChangeReceiver

abstract class BaseConnectionActivity : AppCompatActivity() {
    val network = NetworkChangeReceiver()
    val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
        addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        addAction("android.net.wifi.WIFI_STATE_CHANGED")
    }

    /** Called when returning to the activity */
    override fun onResume() {
        super.onResume()
        registerReceiver(network, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(network)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
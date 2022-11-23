package com.btcpiyush.ads.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.btcpiyush.ads.R
import com.btcpiyush.ads.callback.ConnectionCallback

class NetworkChangeReceiver : BroadcastReceiver() {

    companion object {
        val CONNECTIVITY_CHANGE: String = ConnectivityManager.CONNECTIVITY_ACTION
        val TYPE_WIFI = 1
        val TYPE_MOBILE = 2
        val TYPE_NOT_CONNECTED = 0
        val NETWORK_STATUS_NOT_CONNECTED = 0
        val NETWORK_STATUS_WIFI = 1
        val NETWORK_STATUS_MOBILE = 2

        fun Context.getConnectivityStatus(): Int {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (null != activeNetwork) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) return TYPE_WIFI
                if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE
            }
            return TYPE_NOT_CONNECTED
        }

        fun Context.getConnectivityStatusString(): Int {
            val conn: Int = getConnectivityStatus()
            var status = 0
            if (conn == TYPE_WIFI) {
                status = NETWORK_STATUS_WIFI
            } else if (conn == TYPE_MOBILE) {
                status = NETWORK_STATUS_MOBILE
            } else if (conn == TYPE_NOT_CONNECTED) {
                status = NETWORK_STATUS_NOT_CONNECTED
            }
            return status
        }

    }

    protected var listeners: HashSet<ConnectionCallback>? = HashSet()
    protected var connected: Boolean? = null


    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null || intent == null || intent.getExtras() == null)
            return

        val status: Int = context.getConnectivityStatusString()

        if (CONNECTIVITY_CHANGE == intent.action) {
            if (status == NETWORK_STATUS_NOT_CONNECTED) {
                Toast.makeText(context, context.getString(R.string.no_connection), Toast.LENGTH_SHORT).show()
                connected = false
            } else {
                connected = true
            }
        }

        notifyStateToAll()
    }

    private fun notifyStateToAll() {
        listeners?.let{
            for (listener in it) notifyState(listener)
        }
    }

    private fun notifyState(listener: ConnectionCallback?) {
        if (connected == null || listener == null) return
        if (connected == true) listener.OnConnected() else listener.OnDisConnected()
    }

    fun addListener(l: ConnectionCallback) {
        listeners?.add(l)
        notifyState(l)
    }

    fun removeListener(l: ConnectionCallback) {
        listeners?.remove(l)
    }
}
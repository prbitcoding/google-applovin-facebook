package com.btcpiyush.ads.base

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context, prefName: String) {
    private val context: Context
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        this.context = context
        sharedPreferences =
            context.getSharedPreferences("$prefName", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    /*var curInterestialInterval: Int
        get() = sharedPreferences.getInt("curInterestialInterval", 0)
        set(splashInterestialInterval) {
            splashInterestialInterval.let {
                editor.putInt("curInterestialInterval", it)
                editor.apply()
            }
        }
*/
}


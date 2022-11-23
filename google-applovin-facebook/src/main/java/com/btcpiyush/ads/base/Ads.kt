package com.btcpiyush.ads.base
import android.app.Activity
import android.content.Context
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.btcpiyush.ads.callback.AdLoadCallback
import com.btcpiyush.ads.callback.AdShowCallback

open class Ads (open val adUnitId: String)

open class IntraAds (override val adUnitId: String) : Ads(adUnitId) {
    var isRequested = false
    var isLoaded = false
    var isShowing = false
    var listener: AdLoadCallback? = null
    open fun load(context: Context) {}
    open fun load(context: AppCompatActivity) {}
    open fun show(activity: Activity, listener: AdShowCallback) {}
    open fun dispose() {}
}

open class NativeAds (override val adUnitId: String) : Ads(adUnitId) {
    var isRequested = false
    var isLoaded = false
    var isShowing = false
    var isMediumAds: Boolean = false
    var listener: AdLoadCallback? = null

    open fun load(context: AppCompatActivity) {}
    open fun show(activity: AppCompatActivity) : FrameLayout? {
        return null
    }
    open fun dispose() {}
}

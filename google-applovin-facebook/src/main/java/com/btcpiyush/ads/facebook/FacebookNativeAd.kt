package com.btcpiyush.ads.facebook

import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.btcpiyush.ads.base.BaseNativeAd
import com.btcpiyush.ads.base.NativeAds
import com.facebook.ads.Ad
import com.facebook.ads.NativeAd
import com.facebook.ads.NativeAdListener

class FacebookNativeAd(
    override val adUnitId: String,
    val nativeAdLayout: BaseNativeAd
) : NativeAds(adUnitId) {

    private var nativeAd: NativeAd? = null

    override fun load(activity: AppCompatActivity) {

        Log.e("load: ", " -----  Facebook Native AD Loaded Start ")

        if (isLoaded && !isShowing && !isRequested) {
            listener?.onAdLoaded(this@FacebookNativeAd)
            return@load
        }

        nativeAd = NativeAd(activity, adUnitId)

        val adListener: NativeAdListener = object : NativeAdListener {
            override fun onError(p0: Ad?, p1: com.facebook.ads.AdError?) {
                listener?.onAdLoadError()
            }

            override fun onAdLoaded(p0: Ad?) {
                listener?.onAdLoaded(this@FacebookNativeAd)

            }

            override fun onAdClicked(p0: Ad?) {}

            override fun onLoggingImpression(p0: Ad?) {}

            override fun onMediaDownloaded(p0: Ad?) {}

        }

        nativeAd?.loadAd(
            nativeAd?.buildLoadAdConfig()?.withAdListener(adListener)?.build()
        )
    }

    override fun show(activity: AppCompatActivity) : FrameLayout? {
        isShowing = true
        return if (!isMediumAds) {
            nativeAdLayout.createFacebookNativeAd(activity, nativeAd)
        } else {
            nativeAdLayout.createFacebookNativeAd(activity, nativeAd)
        }
    }

    override fun dispose() {
        nativeAd?.destroy()
        nativeAd = null
    }
}
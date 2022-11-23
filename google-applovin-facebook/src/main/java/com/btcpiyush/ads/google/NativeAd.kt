package com.btcpiyush.ads.google

import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.btcpiyush.ads.base.BaseNativeAd
import com.btcpiyush.ads.base.NativeAds
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd


class NativeAd(
    override val adUnitId: String,
    val nativeAdLayout: BaseNativeAd,
    val adRequest: AdRequest = AdRequest.Builder().build()
) : NativeAds(adUnitId) {

    private var nativeAd: NativeAd? = null

    override fun load(context: AppCompatActivity) {

        Log.e("load: ", " -----  Google Banner AD Loaded Start ")

        if (nativeAd != null) {
            listener?.onAdLoaded(this@NativeAd)
            return@load
        }

        AdLoader.Builder(context, adUnitId)
            .forNativeAd {
                this.nativeAd = it
            }
            .withAdListener(object: AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    listener?.onAdLoaded(this@NativeAd)
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    listener?.onAdLoadError()
                }
            })
            .build()
            .loadAd(adRequest)
    }

    override fun show(activity: AppCompatActivity) : FrameLayout? {
        isShowing = true
        return if (!isMediumAds) {
            nativeAdLayout.createNativeAd(nativeAd, null)
        } else {
            nativeAdLayout.createMediumNativeAd(nativeAd, null)
        }
    }

    override fun dispose() {
        nativeAd?.destroy()
        nativeAd = null
    }
}
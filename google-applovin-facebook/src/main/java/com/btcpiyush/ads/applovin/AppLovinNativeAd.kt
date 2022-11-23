package com.btcpiyush.ads.applovin

import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.btcpiyush.ads.base.BaseNativeAd
import com.btcpiyush.ads.base.CommonAds
import com.btcpiyush.ads.base.NativeAds


class AppLovinNativeAd(
    override val adUnitId: String,
    val nativeAdLayout: BaseNativeAd
) : NativeAds(adUnitId) {

    private var nativeAd: MaxNativeAdLoader? = null
    private var nativeAdView: MaxNativeAdView? = null

    override fun load(context: AppCompatActivity) {

        Log.e("load: ", " -----  Google Banner AD Loaded Start ")

        if (isLoaded && !isShowing && !isRequested) {
            listener?.onAdLoaded(this@AppLovinNativeAd)
            return@load
        }

        CommonAds.flutterAppLovinAds?.let {
            it.getAppLovinSdk()?.let {
                nativeAd = MaxNativeAdLoader(adUnitId, it, context)
                nativeAd?.setNativeAdListener(object: MaxNativeAdListener() {
                    override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd?) {
                        super.onNativeAdLoaded(p0, p1)
                        nativeAdView = p0
                        listener?.onAdLoaded(this@AppLovinNativeAd)
                    }

                    override fun onNativeAdLoadFailed(p0: String?, p1: MaxError?) {
                        super.onNativeAdLoadFailed(p0, p1)
                        listener?.onAdLoadError()
                    }

                    /*override fun onNativeAdExpired(p0: MaxAd?) {
                        super.onNativeAdExpired(p0)

                    }*/

                })

                val nativeAds =  if (!isMediumAds) {
                    nativeAdLayout.createAppLovinNativeAd(context)
                } else {
                    nativeAdLayout.createAppLovinMediumNativeAd(context)
                }

                nativeAd?.loadAd(nativeAds)
                return@load
            }
        }
        listener?.onAdLoadError()
    }

    override fun show(activity: AppCompatActivity) : FrameLayout? {
        isShowing = true
        return nativeAdView
    }

    override fun dispose() {
        nativeAd?.destroy()
        nativeAd = null
        nativeAdView = null
    }
}
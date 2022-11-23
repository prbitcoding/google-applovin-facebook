package com.btcpiyush.ads.applovin


import android.content.res.Resources
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.appharbr.sdk.engine.AdBlockReason
import com.appharbr.sdk.engine.AdSdk
import com.appharbr.sdk.engine.AppHarbr
import com.appharbr.sdk.engine.adformat.AdFormat
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.btcpiyush.ads.base.CommonAds
import com.btcpiyush.ads.base.IntraAds

import com.google.android.gms.ads.AdSize
import java.util.*


class AppLovinBannerAd(override val adUnitId: String, val adSize: AdSize = AdSize.BANNER) : IntraAds(adUnitId) {

    private var bannerAd : MaxAdView? = null

    fun getBannerAd() : MaxAdView? {
        return bannerAd
    }

    override fun load(context: AppCompatActivity) {

        Log.e( "load: ", " -----  Google Banner AD Loaded Start ")

        if (bannerAd != null) {
            listener?.onAdLoaded(this@AppLovinBannerAd)
            return@load
        }

        CommonAds.flutterAppLovinAds?.let{ appLovinWrapper ->
            appLovinWrapper.getAppLovinSdk()?.let{
                bannerAd = MaxAdView(adUnitId, it, context)
                bannerAd?.let {
                    val width = ViewGroup.LayoutParams.MATCH_PARENT
                    it.gravity = Gravity.CENTER
                    it.layoutParams = ViewGroup.LayoutParams(
                        width,
                        (adSize.getHeight() * Resources.getSystem().displayMetrics.density).toInt()
                    )
                    it.setListener(object: MaxAdViewAdListener {
                        override fun onAdLoaded(ad: MaxAd?) {
                            listener?.onAdLoaded(this@AppLovinBannerAd)
                        }

                        override fun onAdDisplayed(ad: MaxAd?) {

                        }

                        override fun onAdHidden(ad: MaxAd?) {

                        }

                        override fun onAdClicked(ad: MaxAd?) {}

                        override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                            dispose()
                            listener?.onAdLoadError()
                        }

                        override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {}

                        override fun onAdExpanded(ad: MaxAd?) {}

                        override fun onAdCollapsed(ad: MaxAd?) {}
                    })

                    if (appLovinWrapper.IsGEOEdgeSDKInitialized()) {
                        AppHarbr.addBannerView(AdSdk.MAX, it, context.lifecycle, appLovinWrapper.getGEOSDKWrapper())
                    }

                    it.loadAd()
                    it.startAutoRefresh()
                }
                return@load
            }
        }

        listener?.onAdLoadError()

    }



    override fun dispose() {
        bannerAd?.destroy()
        bannerAd = null
    }
}
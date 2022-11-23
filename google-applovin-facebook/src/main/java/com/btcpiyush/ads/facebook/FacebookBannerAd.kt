package com.btcpiyush.ads.facebook

import android.content.Context
import android.util.Log
import com.btcpiyush.ads.base.IntraAds
import com.facebook.ads.*

class FacebookBannerAd(override val adUnitId: String, val adSize: AdSize = AdSize.BANNER_HEIGHT_50) : IntraAds(adUnitId) {

    private var bannerAd : AdView? = null

    fun getBannerAd() : AdView? {
        return bannerAd
    }

    override fun load(context: Context) {

        Log.e( "load: ", " -----  Facebook Banner AD Loaded Start ")

        if (isLoaded && !isShowing && !isRequested) {
            listener?.onAdLoaded(this@FacebookBannerAd)
            return@load
        }

        bannerAd = AdView(context, adUnitId, adSize)

        bannerAd?.let {

            val adListener: AdListener = object : AdListener {

                override fun onError(p0: Ad?, p1: AdError?) {
                    Log.e("onAdFailedToLoad: ", " =========  ${p1?.errorMessage}")
                    dispose()
                    listener?.onAdLoadError()
                }

                override fun onAdLoaded(ad: Ad?) {
                    listener?.onAdLoaded(this@FacebookBannerAd)
                }

                override fun onAdClicked(ad: Ad?) {

                }

                override fun onLoggingImpression(ad: Ad?) {

                }

            }

            it.loadAd(it.buildLoadAdConfig().withAdListener(adListener).build())

        }
    }

    override fun dispose() {
        bannerAd?.destroy()
        bannerAd = null
    }
}
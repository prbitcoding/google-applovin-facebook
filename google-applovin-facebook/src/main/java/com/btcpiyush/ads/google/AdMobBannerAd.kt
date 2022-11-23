package com.btcpiyush.ads.google

import android.content.Context
import android.util.Log
import com.btcpiyush.ads.base.IntraAds

import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView

class AdMobBannerAd(override val adUnitId: String, val adSize: AdSize = AdSize.BANNER) : IntraAds(adUnitId) {

    private var bannerAd : AdManagerAdView? = null

    fun getAbMobBannerAd() : AdManagerAdView? {
        return bannerAd
    }

    override fun load(context: Context) {

        Log.e( "load: ", " -----  Google Banner AD Loaded Start ")

        if (bannerAd != null) {
            listener?.onAdLoaded(this@AdMobBannerAd)
            return@load
        }

        bannerAd = AdManagerAdView(context)
        bannerAd?.let {
            it.adUnitId = adUnitId
            it.setAdSize(adSize)
            it.loadAd(AdManagerAdRequest.Builder().build())
            it.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    listener?.onAdLoaded(this@AdMobBannerAd)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    dispose()
                    listener?.onAdLoadError()
                }

                override fun onAdOpened() {}
                override fun onAdClicked() {}
                override fun onAdClosed() {}
            }
        }
    }

    override fun dispose() {
        bannerAd?.destroy()
        bannerAd = null
    }
}
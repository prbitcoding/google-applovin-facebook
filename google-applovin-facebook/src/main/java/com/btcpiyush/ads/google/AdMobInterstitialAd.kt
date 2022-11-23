package com.btcpiyush.ads.google

import android.app.Activity
import android.content.Context
import android.util.Log
import com.btcpiyush.ads.base.IntraAds
import com.btcpiyush.ads.callback.AdShowCallback
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback

class AdMobInterstitialAd(override val adUnitId: String) : IntraAds(adUnitId) {

    private var intraAd : AdManagerInterstitialAd? = null

    override fun load(context: Context) {
        Log.e( "load: ", " -----  Admanger AD Loaded Start ")
        if (intraAd != null) {
            listener?.onAdLoaded(this@AdMobInterstitialAd)
            return@load
        }

        AdManagerInterstitialAd.load(context, adUnitId, AdManagerAdRequest.Builder().build(), object: AdManagerInterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: AdManagerInterstitialAd) {
                super.onAdLoaded(ad)
                intraAd = ad
                listener?.onAdLoaded(this@AdMobInterstitialAd)
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                dispose()
                listener?.onAdLoadError()
            }
        })
    }

    override fun show(activity: Activity, listener: AdShowCallback) {
        intraAd?.let {
            it.fullScreenContentCallback = object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    listener.onAdDismiss()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    listener.onAdFailedToShow()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    listener.onAdImpression()
                }

            }
            it.show(activity)
        }
    }

    override fun dispose() {
        intraAd = null
    }
}
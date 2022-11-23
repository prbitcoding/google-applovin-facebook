package com.btcpiyush.ads.google

import android.app.Activity
import android.content.Context
import android.util.Log
import com.btcpiyush.ads.base.IntraAds
import com.btcpiyush.ads.callback.AdShowCallback
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialAd(override val adUnitId: String) : IntraAds(adUnitId) {

    private var intraAd : InterstitialAd? = null

    override fun load(context: Context) {

        Log.e( "load: ", " -----  Google AD Loaded Start ")

        if (intraAd != null) {
            listener?.onAdLoaded(this@InterstitialAd)
            return@load
        }

        InterstitialAd.load(context, adUnitId, AdRequest.Builder().build(), object: InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                super.onAdLoaded(ad)
                intraAd = ad
                listener?.onAdLoaded(this@InterstitialAd)
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                dispose()
                listener?.onAdLoadError()
            }
        })
    }

    override fun show(activity: Activity, listner: AdShowCallback) {
        intraAd?.let {
            it.fullScreenContentCallback = object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    listner.onAdDismiss()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    listner.onAdFailedToShow()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    listner.onAdImpression()
                }

            }
            it.show(activity)
        } ?: kotlin.run {
            listener?.onAdLoadError()
        }
    }

    override fun dispose() {
        intraAd = null
    }
}
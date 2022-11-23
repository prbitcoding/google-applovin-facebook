package com.btcpiyush.ads.facebook

import android.app.Activity
import android.content.Context
import android.util.Log
import com.btcpiyush.ads.base.IntraAds
import com.btcpiyush.ads.callback.AdShowCallback
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener

class FacebookInterstitialAd(override val adUnitId: String) : IntraAds(adUnitId) {

    private var intraAd : InterstitialAd? = null

    override fun load(context: Context) {

        Log.e( "load: ", " -----  Facebook AD Loaded Start ")

        if (isLoaded && !isShowing && !isRequested) {
            listener?.onAdLoaded(this@FacebookInterstitialAd)
            return@load
        }

        val callback = object: InterstitialAdListener {
            override fun onError(p0: Ad?, p1: AdError?) {
                Log.e( "onError: ", "Facebook Loaded  Error  ${p1?.errorCode} ${p1?.errorMessage}")
                dispose()
                listener?.onAdLoadError()
            }

            override fun onAdLoaded(p0: Ad?) {
                listener?.onAdLoaded(this@FacebookInterstitialAd)
            }

            override fun onAdClicked(p0: Ad?) {}

            override fun onLoggingImpression(p0: Ad?) {}

            override fun onInterstitialDisplayed(p0: Ad?) {}

            override fun onInterstitialDismissed(p0: Ad?) {}
        }

        intraAd = InterstitialAd(context, adUnitId)
        intraAd?.loadAd(
            intraAd?.buildLoadAdConfig()
                ?.withAdListener(callback)?.build()
        )
    }

    override fun show(activity: Activity, listner: AdShowCallback) {
        intraAd?.let {
            val callback = object: InterstitialAdListener {
                override fun onError(p0: Ad?, p1: AdError?) {
                    listner.onAdFailedToShow()
                }

                override fun onAdLoaded(p0: Ad?) {}

                override fun onAdClicked(p0: Ad?) {}

                override fun onLoggingImpression(p0: Ad?) {
                    listner.onAdImpression()
                }

                override fun onInterstitialDisplayed(p0: Ad?) {}

                override fun onInterstitialDismissed(p0: Ad?) {
                    listner.onAdDismiss()
                }
            }

            it.buildLoadAdConfig().withAdListener(callback).build()
            it.show()
        } ?: kotlin.run {
            listener?.onAdLoadError()
        }
    }

    override fun dispose() {
        intraAd?.destroy()
        intraAd = null
    }
}
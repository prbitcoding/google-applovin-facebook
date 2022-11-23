package com.btcpiyush.ads.google

import android.app.Activity
import android.content.Context
import android.util.Log
import com.btcpiyush.ads.base.IntraAds
import com.btcpiyush.ads.callback.AdShowCallback
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.*

class RewardAd(override val adUnitId: String, val adRequest: AdRequest) : IntraAds(adUnitId) {

    private var rewardAd : RewardedAd? = null

    var isVideoRewarded = false

    override fun load(context: Context) {

        Log.e( "load: ", " -----  Google AD Loaded Start ")

        if (rewardAd != null) {
            listener?.onAdLoaded(this@RewardAd)
            return@load
        }
        isVideoRewarded = false
        RewardedAd.load(context, adUnitId, adRequest, object: RewardedAdLoadCallback() {

            override fun onAdLoaded(ad: RewardedAd) {
                super.onAdLoaded(ad)
                rewardAd = ad
                listener?.onAdLoaded(this@RewardAd)
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                dispose()
                listener?.onAdLoadError()
            }
        })
    }

    override fun show(activity: Activity, listenr: AdShowCallback) {
        rewardAd?.let {
            it.fullScreenContentCallback = object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    listenr.onAdDismiss()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    listenr.onAdFailedToShow()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    listenr.onAdImpression()
                }

            }

            it.show(activity) {
                isVideoRewarded = true
            }
        } ?: kotlin.run {
            listener?.onAdLoadError()
        }
    }

    override fun dispose() {
        rewardAd = null
    }
}
package com.btcpiyush.ads.facebook

import android.app.Activity
import android.content.Context
import android.util.Log
import com.btcpiyush.ads.base.IntraAds
import com.btcpiyush.ads.callback.AdShowCallback
import com.facebook.ads.*

class FacebookRewardAd(override val adUnitId: String) : IntraAds(adUnitId) {

    private var rewardAd : RewardedVideoAd? = null

    var isVideoRewarded = false

    override fun load(context: Context) {

        Log.e( "load: ", " -----  Facebook AD Loaded Start ")

        if (isLoaded && !isShowing && !isRequested) {
            listener?.onAdLoaded(this@FacebookRewardAd)
            return@load
        }
        isVideoRewarded = false

        val callback = object: RewardedVideoAdListener {
            override fun onError(p0: Ad?, p1: AdError?) {
                dispose()
                listener?.onAdLoadError()
            }

            override fun onAdLoaded(p0: Ad?) {
                listener?.onAdLoaded(this@FacebookRewardAd)
            }

            override fun onAdClicked(p0: Ad?) {

            }

            override fun onLoggingImpression(p0: Ad?) {

            }

            override fun onRewardedVideoCompleted() {
                isVideoRewarded = true
            }

            override fun onRewardedVideoClosed() {

            }
        }

        rewardAd = RewardedVideoAd(context, adUnitId)
        rewardAd?.loadAd(
            rewardAd?.buildLoadAdConfig()
                ?.withAdListener(callback)?.build()
        )
    }

    override fun show(activity: Activity, listenr: AdShowCallback) {
        rewardAd?.let { it ->
            val callback = object: RewardedVideoAdListener {
                override fun onError(p0: Ad?, p1: AdError?) {
                    listenr.onAdFailedToShow()
                }

                override fun onAdLoaded(p0: Ad?) {}

                override fun onAdClicked(p0: Ad?) {}

                override fun onLoggingImpression(p0: Ad?) {
                    listenr.onAdImpression()
                }

                override fun onRewardedVideoCompleted() {
                    isVideoRewarded = true
                }

                override fun onRewardedVideoClosed() {
                    listenr.onAdDismiss()
                }
            }
            it.buildLoadAdConfig().withAdListener(callback).build()
            it.show()
        } ?: kotlin.run {
            listener?.onAdLoadError()
        }
    }

    override fun dispose() {
        rewardAd?.destroy()
        rewardAd = null
    }
}
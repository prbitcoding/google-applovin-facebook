package com.btcpiyush.ads.applovin

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
/*import com.appharbr.sdk.engine.AdSdk
import com.appharbr.sdk.engine.AdStateResult
import com.appharbr.sdk.engine.AppHarbr*/
import com.applovin.mediation.*
import com.applovin.mediation.ads.MaxRewardedAd
import com.btcpiyush.ads.base.CommonAds
import com.btcpiyush.ads.base.IntraAds
import com.btcpiyush.ads.callback.AdShowCallback

class AppLovinRewardAd(override val adUnitId: String) : IntraAds(adUnitId) {

    private var rewardAd: MaxRewardedAd? = null

    var isVideoRewarded = false

    override fun load(activity: AppCompatActivity) {

        Log.e("load: ", " -----  AppLovin AD Loaded Start ")

        if (isLoaded && !isShowing && !isRequested) {
            listener?.onAdLoaded(this@AppLovinRewardAd)
            return@load
        }

        isVideoRewarded = false
        CommonAds.flutterAppLovinAds?.let { appLovinWrapper ->
            appLovinWrapper.getAppLovinSdk()?.let {
                rewardAd = MaxRewardedAd.getInstance(adUnitId, it, activity)

                var callback : MaxRewardedAdListener? = object : MaxRewardedAdListener {
                    override fun onAdLoaded(ad: MaxAd?) {

                        listener?.onAdLoaded(this@AppLovinRewardAd)
                    }

                    override fun onAdDisplayed(ad: MaxAd?) {
                        Log.e("TAG", "onAdDisplayed: -----OLD-------   ")
                    }

                    override fun onAdHidden(ad: MaxAd?) {
                        Log.e("TAG", "onAdHidden: -----OLD-------   ")
                    }

                    override fun onAdClicked(ad: MaxAd?) {}

                    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                        dispose()
                        listener?.onAdLoadError()
                    }

                    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {}

                    override fun onUserRewarded(ad: MaxAd?, reward: MaxReward?) {}

                    override fun onRewardedVideoStarted(ad: MaxAd?) {}

                    override fun onRewardedVideoCompleted(ad: MaxAd?) {}

                }

                /*if (appLovinWrapper.IsGEOEdgeSDKInitialized()) {
                    callback =
                        AppHarbr.addRewardedAd(
                            AdSdk.MAX,
                            rewardAd!!,
                            callback!!,
                            activity.lifecycle,
                            appLovinWrapper.getGEOSDKWrapper()
                        )
                }*/

                rewardAd?.setListener(callback)
                rewardAd?.loadAd()
                return@load
            }
        }
        listener?.onAdLoadError()
    }

    override fun show(activity: Activity, listenr: AdShowCallback) {
        rewardAd?.let {
            if (it.isReady) {

                /*if (CommonAds.flutterAppLovinAds?.IsGEOEdgeSDKInitialized() == true) {
                    val rewardState = AppHarbr.getRewardedState(it)
                    // Toast.makeText(activity, "onAdLoaded apploving: status ------ >"+interstitialState, Toast.LENGTH_LONG).show()
                    if (rewardState == AdStateResult.BLOCKED) {
                        listener?.onAdLoadError()
                        return@show
                    }
                }*/

                it.setListener(object : MaxRewardedAdListener {

                    override fun onAdLoaded(ad: MaxAd?) {}

                    override fun onAdDisplayed(ad: MaxAd?) {
                        Log.e("TAG", "onAdDisplayed: -----NEW-------   ")
                        listenr.onAdImpression()
                    }

                    override fun onAdHidden(ad: MaxAd?) {
                        Log.e("TAG", "onAdHidden: -----NEW---------   ")
                        listenr.onAdDismiss()
                    }

                    override fun onAdClicked(ad: MaxAd?) {}

                    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {}

                    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                        listenr.onAdFailedToShow()
                    }

                    override fun onUserRewarded(ad: MaxAd?, reward: MaxReward?) {
                        isVideoRewarded = true
                    }

                    override fun onRewardedVideoStarted(ad: MaxAd?) {}

                    override fun onRewardedVideoCompleted(ad: MaxAd?) {
                        isVideoRewarded = true
                    }

                })
                it.showAd()
            } else {
                listener?.onAdLoadError()
            }
        } ?: kotlin.run {
            listener?.onAdLoadError()
        }
    }

    override fun dispose() {
        rewardAd?.destroy()
        rewardAd = null
    }
}
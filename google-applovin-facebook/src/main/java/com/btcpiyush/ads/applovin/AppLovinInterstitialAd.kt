package com.btcpiyush.ads.applovin

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
/*import com.appharbr.sdk.engine.AdBlockReason
import com.appharbr.sdk.engine.AdSdk
import com.appharbr.sdk.engine.AdStateResult
import com.appharbr.sdk.engine.AppHarbr
import com.appharbr.sdk.engine.adformat.AdFormat*/
import com.applovin.mediation.*
import com.applovin.mediation.ads.MaxInterstitialAd
import com.btcpiyush.ads.base.CommonAds
import com.btcpiyush.ads.base.IntraAds
import com.btcpiyush.ads.callback.AdShowCallback
import java.util.*

class AppLovinInterstitialAd(override val adUnitId: String) : IntraAds(adUnitId) {

    private var intraAd : MaxInterstitialAd? = null

    override fun load(context: AppCompatActivity) {

        Log.e( "load: ", " -----  AppLovin AD Loaded Start ")

        if (isLoaded && !isShowing && !isRequested) {
            listener?.onAdLoaded(this@AppLovinInterstitialAd)
            return@load
        }

        CommonAds.flutterAppLovinAds?.let { appLovinWrapper ->
            appLovinWrapper.getAppLovinSdk()?.let {
                intraAd = MaxInterstitialAd(
                    adUnitId, it, context
                )

                var callback : MaxAdListener? = object: MaxAdListener{
                    override fun onAdLoaded(ad: MaxAd?) {
                        listener?.onAdLoaded(this@AppLovinInterstitialAd)
                    }

                    override fun onAdDisplayed(ad: MaxAd?) {

                    }

                    override fun onAdHidden(ad: MaxAd?) {
                    }

                    override fun onAdClicked(ad: MaxAd?) {

                    }

                    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                        dispose()
                        listener?.onAdLoadError()
                    }

                    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {

                    }
                }

                /*if (appLovinWrapper.IsGEOEdgeSDKInitialized()) {
                    callback =
                        AppHarbr.addInterstitial(
                            AdSdk.MAX,
                            intraAd!!,
                            callback!!,
                            context.lifecycle,
                            appLovinWrapper.getGEOSDKWrapper()
                        )
                }*/

                intraAd?.setListener(callback)
                intraAd?.loadAd()
                return@load
            }
        }
        listener?.onAdLoadError()
    }

    override fun show(activity: Activity, listenr: AdShowCallback) {
        intraAd?.let {
            if (it.isReady) {

                /*if (CommonAds.flutterAppLovinAds?.IsGEOEdgeSDKInitialized() == true) {
                    val interstitialState = AppHarbr.getInterstitialState(it)
                    // Toast.makeText(activity, "onAdLoaded apploving: status ------ >"+interstitialState, Toast.LENGTH_LONG).show()
                    if (interstitialState == AdStateResult.BLOCKED) {
                        listener?.onAdLoadError()
                        return@show
                    }
                }*/

                it.setListener(object: MaxAdListener {
                    override fun onAdLoaded(ad: MaxAd?) {}

                    override fun onAdDisplayed(ad: MaxAd?) {
                        listenr.onAdImpression()
                    }

                    override fun onAdHidden(ad: MaxAd?) {
                        listenr.onAdDismiss()
                    }

                    override fun onAdClicked(ad: MaxAd?) {}

                    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {}

                    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                        listenr.onAdFailedToShow()
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
        intraAd?.destroy()
        intraAd = null
    }
}
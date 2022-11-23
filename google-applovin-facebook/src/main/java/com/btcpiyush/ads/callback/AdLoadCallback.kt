package com.btcpiyush.ads.callback

import com.btcpiyush.ads.base.Ads

interface AdLoadCallback {
    fun onAdLoaded(ads: Ads)
    fun onAdLoadError()
}
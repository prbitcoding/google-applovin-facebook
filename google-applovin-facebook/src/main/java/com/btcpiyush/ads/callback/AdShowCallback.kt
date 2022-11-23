package com.btcpiyush.ads.callback

interface AdShowCallback {
    fun onAdDismiss()
    fun onAdFailedToShow()
    fun onAdImpression()
}
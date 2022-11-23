package com.btcpiyush.ads.base

import com.btcpiyush.ads.applovin.AppLovinWrapper
import com.btcpiyush.ads.facebook.FacebookWrapper
import com.btcpiyush.ads.google.MobileAdsWrapper

object CommonAds {
    var interestialAdTimeInterval: Long = 0L
    var flutterMobileAds: MobileAdsWrapper? = null
    var flutterAppLovinAds: AppLovinWrapper? = null
    var flutterFacebookAds: FacebookWrapper? = null
    val nativeAds: ArrayList<NativeAds?> = ArrayList()
    val intraAds: ArrayList<IntraAds?> = ArrayList()
    val rewardAds: ArrayList<IntraAds?> = ArrayList()
    val bannerAds: ArrayList<IntraAds?> = ArrayList()
}
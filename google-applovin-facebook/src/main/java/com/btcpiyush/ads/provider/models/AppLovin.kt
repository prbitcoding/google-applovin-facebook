package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class AppLovin(
    @SerializedName("sdk_key") var sdkKey: String? = null,
    @SerializedName("is_unlimited_ads") var isUnlimitedAds: Boolean? = null,
    @SerializedName("id_unit_open") var idUnitOpen: String? = null,
    @SerializedName("id_unit_banner") var appLovinBannerID: String? = null,
    @SerializedName("id_unit_intra") var interestialId: String? = null,
    @SerializedName("id_unit_native_multiple") var appLovinNativeIDs: ApplovinNativeAd? = null,
    @SerializedName("rewared_ads_unit_id") var rewaredId: String? = null,
    @SerializedName("is_open_ads") var isOpenAds: Boolean? = null,
    @SerializedName("is_banner_ads") var isBannerAds: Boolean? = null,
    @SerializedName("is_interstitial_ads") var isInterstitialAds: Boolean? = null,
    @SerializedName("is_native_ads") var isNativeAds: Boolean? = null,
    @SerializedName("is_rewared_ads") var isRewaredAds: Boolean? = null
)
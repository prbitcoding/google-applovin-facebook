package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class GoogleAds(
    @SerializedName("banner_ad_unit_id_android") var bannerId: String? = null,
    @SerializedName("interstitial_ad_unit_id_android") var interestialId: String? = null,
    @SerializedName("app_open_ad_unit_id_android") var appOpenAdUnitIdAndroid: String? = null,
    @SerializedName("native_ads_unit_id_android") var nativeId: String? = null,
    @SerializedName("rewared_ads_unit_id") var rewaredId: String? = null,
    @SerializedName("native_video_ads_unit_id_android") var nativeVideoId: String? = null,
    @SerializedName("is_banner_ads") var isBannerAds: Boolean? = null,
    @SerializedName("is_open_ads") var isOpenAds: Boolean? = null,
    @SerializedName("is_interstitial_ads") var isInterstitialAds: Boolean? = null,
    @SerializedName("is_rewared_ads") var isRewaredAds: Boolean? = null,
    @SerializedName("is_native_ads") var isNativeAds: Boolean? = null,
    @SerializedName("no_of_click_interstitial") var intraAdClickInterval: Int? = null
)
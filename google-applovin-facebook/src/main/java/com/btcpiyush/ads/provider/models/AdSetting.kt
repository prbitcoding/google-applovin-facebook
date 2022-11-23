package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class AdSetting(
    @SerializedName("app_version_code") var appVersionCode: Int? = null,
    @SerializedName("is_full_ads") var isFullAds: Boolean? = null,
    @SerializedName("onesignal_id") var onesignalId: String? = null,
    @SerializedName("is_geoedge_sdk_flag") var isGeoedgeSdkFlag: Boolean? = null,
    @SerializedName("geoedge_sdk_key") var geoedgeSdkKey: String? = null,
    @SerializedName("ads_loading_text") var adsLoadingText: String? = null,
    @SerializedName("seconds_require_triger_reward") var secondsRequireTrigerReward: Int? = null,
    @SerializedName("global_intra_interval_click") var intraAdClickInterval: Int? = null,
    @SerializedName("interstitial_ad_time_interval") var intraAdTimeInterval: Int? = null,
    @SerializedName("native_ad_list_interval") var nativeAdListInterval: Int? = null,
    var intraAdClickCount: Int = 0
)
package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class ApplovinNativeAd(
    @SerializedName("small") var small: String? = null,
    @SerializedName("medium") var medium: String? = null,
    @SerializedName("manual") var manual: String? = null
)
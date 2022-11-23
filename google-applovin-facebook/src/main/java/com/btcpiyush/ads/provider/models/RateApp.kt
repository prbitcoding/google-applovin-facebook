package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class RateApp(
    @SerializedName("is_rate_app") var isRateApp: Boolean? = null,
    @SerializedName("package_id") var packageId: String? = null,
    @SerializedName("rate_app_url") var rateAppUrl: String? = null,
    @SerializedName("rate_message") var rateMessage: String? = null
)
package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class PrivacyPolicy(
    @SerializedName("is_privacy_policy") var isPrivacyPolicy: Boolean? = null,
    @SerializedName("is_system_browser") var isSystemBrowser: Boolean? = null,
    @SerializedName("privacy_policy") var privacyPolicy: String? = null
)
package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class ShareApp(
    @SerializedName("is_share_app") var isShareApp: Boolean? = null,
    @SerializedName("share_title") var shareTitle: String? = null,
    @SerializedName("share_message") var shareMessage: String? = null
)
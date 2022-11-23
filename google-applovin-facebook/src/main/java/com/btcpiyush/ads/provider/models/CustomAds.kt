package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class CustomAds(
    @SerializedName("qureka_link") var qurekaLink: String? = null,
    @SerializedName("is_quraka_banner") var isQurakaBanner: Boolean? = null,
    @SerializedName("is_quraka_intera") var isQurakaIntera: Boolean? = null,
    @SerializedName("is_quraka_native") var isQurakaNative: Boolean? = null,
    @SerializedName("close_button_to_direct_web") var closeButtonToDirectWeb: Boolean? = null,
    @SerializedName("quraka_intra_image") var qurakaIntraImage: ArrayList<String>? = null,
    @SerializedName("quraka_native_image") var qurakaNativeImage: ArrayList<String>? = null,
    @SerializedName("quraka_banner_image") var customBannerImage: ArrayList<String>? = null
)
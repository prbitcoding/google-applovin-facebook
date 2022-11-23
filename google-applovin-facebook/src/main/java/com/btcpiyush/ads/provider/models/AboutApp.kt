package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class AboutApp(
    @SerializedName("is_override") var isOverride: Boolean? = null,
    @SerializedName("is_about_app") var isAboutApp: Boolean? = null,
    @SerializedName("about_title") var aboutTitle: String? = null,
    @SerializedName("about_message") var aboutMessage: String? = null,
    @SerializedName("registration_detail") var registrationDetail: String? = null,
    @SerializedName("version_detail") var versionDetail: String? = null
)
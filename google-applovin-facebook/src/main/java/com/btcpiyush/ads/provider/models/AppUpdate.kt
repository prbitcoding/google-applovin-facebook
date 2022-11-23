package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class AppUpdate(
    @SerializedName("is_popup_dialog") var isPopupDialog: Boolean? = null,
    @SerializedName("is_update_require") var isUpdateRequire: Boolean? = null,
    @SerializedName("package_id") var packageId: String? = null,
    @SerializedName("website_url") var websiteUrl: String? = null,
    @SerializedName("app_icon") var appIcon: String? = null,
    @SerializedName("updated_version_code") var updatedVersionCode: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("default_message") var defaultMessage: String? = null
)
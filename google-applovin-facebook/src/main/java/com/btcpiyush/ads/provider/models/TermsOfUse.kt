package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class TermsOfUse(
    @SerializedName("is_terms_of_use") var isTermsOfUse: Boolean? = null,
    @SerializedName("is_system_browser") var isSystemBrowser: Boolean? = null,
    @SerializedName("terms_of_use") var termsOfUse: String? = null
)
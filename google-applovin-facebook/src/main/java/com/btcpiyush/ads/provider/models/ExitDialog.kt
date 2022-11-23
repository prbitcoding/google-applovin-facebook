package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class ExitDialog(
    @SerializedName("is_exit_require") var isExitRequire: Boolean? = null,
    @SerializedName("exit_title") var exitTitle: String? = null,
    @SerializedName("exit_message") var exitMessage: String? = null
)
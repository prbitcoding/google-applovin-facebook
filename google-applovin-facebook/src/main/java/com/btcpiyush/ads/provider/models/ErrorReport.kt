package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class ErrorReport(
    @SerializedName("is_error_report_support") var isErrorReportSupport: Boolean? = null,
    @SerializedName("email_id") var emailId: String? = null
)
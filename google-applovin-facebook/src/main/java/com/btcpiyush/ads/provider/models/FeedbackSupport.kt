package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class FeedbackSupport(
    @SerializedName("is_feedback_support") var isFeedbackSupport: Boolean? = null,
    @SerializedName("email_id") var emailId: String? = null,
    @SerializedName("feedback_title") var feedbackTitle: String? = null,
    @SerializedName("feedback_message") var feedbackMessage: String? = null,
    @SerializedName("email_subject") var emailSubject: String? = null,
    @SerializedName("email_message") var emailMessage: String? = null
)
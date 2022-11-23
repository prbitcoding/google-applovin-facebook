package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName

open class Settings(
    @SerializedName("app_logo") var appLogo: String? = null,
    @SerializedName("app_title") var appTitle: String? = null,
    @SerializedName("ads_sequence") var adsSequence: ArrayList<String>? = null,
    @SerializedName("more_app_url") var moreAppUrl: String? = null,
    @SerializedName("extra_url") var extraUrl: String? = null,
    @SerializedName("more_live_apps") var moreLiveApps: String? = null,
    @SerializedName("iptv_player_url") var iptvPlayerUrl: String? = null,
    @SerializedName("ad_setting") var adSetting: AdSetting? = null,
    @SerializedName("app_update") var appUpdate: AppUpdate? = null,
    @SerializedName("exit_dialog") var exitDialog: ExitDialog? = null,
    @SerializedName("reward_dialog") var rewardDialog: RewardDialog? = null,
    @SerializedName("About_App") var AboutApp: AboutApp? = null,
    @SerializedName("rate_app") var rateApp: RateApp? = null,
    @SerializedName("share_app") var shareApp: ShareApp? = null,
    @SerializedName("privacy_policy") var privacyPolicy: PrivacyPolicy? = null,
    @SerializedName("terms_of_use") var termsOfUse: TermsOfUse? = null,
    @SerializedName("error_report") var errorReport: ErrorReport? = null,
    @SerializedName("feedback_support") var feedbackSupport: FeedbackSupport? = null,
    @SerializedName("google_ads") var googleAds: GoogleAds? = null,
    @SerializedName("facebook_ads") var facebookAds: FacebookAds? = null,
    @SerializedName("app_lovin") var appLovin: AppLovin? = null,
    @SerializedName("custom_ads") var customAds: CustomAds? = null
)
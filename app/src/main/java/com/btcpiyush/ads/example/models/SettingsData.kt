package com.btcpiyush.ads.example.models

import com.btcpiyush.ads.provider.models.Settings
import com.google.gson.annotations.SerializedName

data class SettingsData(
  @SerializedName("com.bitlinks.korea.iptv.m3ulist") var settings: Settings? = null
)
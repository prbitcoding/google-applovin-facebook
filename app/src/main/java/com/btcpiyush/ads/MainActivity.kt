package com.btcpiyush.ads

import android.content.Intent
import android.widget.Button

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.btcpiyush.ads.base.BaseAdActivity
import com.btcpiyush.ads.databinding.ActivityMainBinding
import com.btcpiyush.ads.models.SettingsData
import com.btcpiyush.ads.provider.api.AdsRetrofit
import com.btcpiyush.ads.provider.api.AdsResponse
import com.btcpiyush.ads.provider.models.Settings

class MainActivity : BaseAdActivity() {

    companion object {
        var adsSettings: SettingsData? = null
    }

    private lateinit var nextLevelButton: Button
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create the next level button, which tries to show an interstitial when clicked.
        nextLevelButton = binding.nextLevelButton
        nextLevelButton.isEnabled = true

        AdsRetrofit.getAdsSettings(
            "https://gitlab.com/bitcodingsolutions/iptv_channels/-/raw/main/All_Country_App/Korea/",
            "App_Setting.json",
            SettingsData::class.java,
            object : AdsResponse<SettingsData> {
                override fun onResponse(response: SettingsData?) {
                    adsSettings = response
                    response?.settings?.adsSequence?.clear()
                    response?.settings?.adsSequence?.add("facebook_ads")
                    response?.settings?.facebookAds?.interestialId = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
                    initializeSDK()
                    Log.e(TAG, "onResponse: ${adsSettings?.settings?.appLogo}")
                    showBannerAd()
                    Log.e(
                        TAG,
                        "onResponse:   ------- ${adsSettings?.settings?.adSetting?.onesignalId}"
                    )
                }

                override fun onFailure(t: Exception?) {
                    Log.e(TAG, "onFailure: ${t?.message}")
                    t?.printStackTrace()
                }
            })

        nextLevelButton.setOnClickListener {
            showRewardAds {
                startActivity(Intent(this@MainActivity, NewActivty::class.java))
            }
        }
    }

    override fun nativeContainer(): FrameLayout? {
        return null
    }

    override fun getAdSetting(): Settings? {
        return adsSettings?.settings
    }

    override fun curAppVersionCode(): Int {
        return BuildConfig.VERSION_CODE
    }

    override fun bannerContainer(): FrameLayout? {
        return binding.bottomBanner
    }
}
package com.btcpiyush.ads.example

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.btcpiyush.ads.base.BaseAdActivity
import com.btcpiyush.ads.example.databinding.ActivityNewActivtyBinding
import com.btcpiyush.ads.provider.models.Settings

class NewActivty : BaseAdActivity() {

    private lateinit var binding: ActivityNewActivtyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showNativeAd()
        binding.nextLevelButton.setOnClickListener {
            showIntraAds {
                Toast.makeText(
                    this@NewActivty,
                    "Next Intra Callback is Completed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun getAdSetting(): Settings? {
        return MainActivity.adsSettings?.settings
    }

    override fun nativeContainer(): FrameLayout? {
        return binding.bottomNative
    }

    override fun curAppVersionCode(): Int {
        return BuildConfig.VERSION_CODE
    }

    override fun bannerContainer(): FrameLayout? {
        return null
    }
}
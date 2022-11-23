package com.btcpiyush.ads.base

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.applovin.sdk.AppLovinMediationProvider
import com.btcpiyush.ads.R
import com.btcpiyush.ads.applovin.*
import com.btcpiyush.ads.callback.AdLoadCallback
import com.btcpiyush.ads.callback.AdShowCallback
import com.btcpiyush.ads.facebook.*
import com.btcpiyush.ads.google.*
import com.btcpiyush.ads.provider.models.Settings
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import java.util.*

abstract class BaseAdActivity : BaseConnectionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun initializeSDK() {
        getAdSetting()?.let {
            if (it.adsSequence?.contains(getString(R.string.admanager)) == true || it.adsSequence?.contains(
                    getString(R.string.google_ads)
                ) == true
            ) {

                if (CommonAds.flutterMobileAds == null) {
                    CommonAds.flutterMobileAds = MobileAdsWrapper()
                }

                CommonAds.flutterMobileAds?.initialize(this.applicationContext) { }
            }

            if (it.adsSequence?.contains(getString(R.string.facebook_ads)) == true) {

                if (CommonAds.flutterFacebookAds == null) {
                    CommonAds.flutterFacebookAds = FacebookWrapper()
                }

                CommonAds.flutterFacebookAds?.initialize(this.applicationContext)
            }

            if (it.adsSequence?.contains(getString(R.string.app_lovin)) == true) {
                if (it.appLovin?.sdkKey?.isNotEmpty() == true) {

                    if (CommonAds.flutterAppLovinAds == null) {
                        CommonAds.flutterAppLovinAds = AppLovinWrapper()
                    }

                    CommonAds.flutterAppLovinAds?.initializeSDK(
                        this.applicationContext,
                        (it.appLovin?.sdkKey ?: "")
                    )
                    CommonAds.flutterAppLovinAds?.setVerboseLogging(true)
                    CommonAds.flutterAppLovinAds?.setMediationProvider(AppLovinMediationProvider.MAX)
                    CommonAds.flutterAppLovinAds?.initialize {}
                    if (it.adSetting?.isGeoedgeSdkFlag == true) {
                        CommonAds.flutterAppLovinAds?.initGEOEdgeSDK(
                            this.applicationContext,
                            (it.adSetting?.geoedgeSdkKey ?: "")
                        )
                    }
                }
            }
        }
    }

    abstract fun getAdSetting(): Settings?

    abstract fun nativeContainer(): FrameLayout?

    abstract fun curAppVersionCode(): Int

    abstract fun bannerContainer(): FrameLayout?

    fun showBannerAd(pos: Int = 0) {
        bannerContainer()?.let {
            var adsId = CommonAds.bannerAds.size
            if (adsId > 0) {
                try {
                    adsId =
                        CommonAds.bannerAds.indexOfFirst { intraAds -> (intraAds?.isRequested != true) && (intraAds?.isLoaded == true) && (intraAds?.isShowing != true) }
                } catch (exp: Exception) {
                    try {
                        adsId = CommonAds.bannerAds.indexOfFirst { intraAds -> intraAds == null }
                    } catch (exp: Exception) {
                    }
                }
            }

            if (adsId < 0) {
                adsId = CommonAds.bannerAds.size
            }

            getAdSetting()?.let { adMob ->
                if ((adMob.adsSequence?.size ?: 0) > pos) {
                    if (curAppVersionCode() < (adMob.adSetting?.appVersionCode ?: 0)) {
                        when (adMob.adsSequence?.get(pos)?.lowercase() ?: false) {
                            getString(R.string.admanager).lowercase(),
                            getString(R.string.google_ads).lowercase() -> {
                                if (adMob.googleAds?.isBannerAds == true) {
                                    val bannerID = adMob.googleAds?.bannerId ?: ""
                                    if (!(bannerID.isNullOrEmpty())) {

                                        if (getString(R.string.admanager).lowercase().equals(
                                                adMob.adsSequence?.get(
                                                    pos
                                                )?.lowercase()
                                            )
                                        ) {
                                            loadAdMobBanner(bannerID, adsId, pos)
                                            return@showBannerAd
                                        }
                                        loadBanner(bannerID, adsId, pos)
                                        return@showBannerAd
                                    }
                                }
                                showBannerAd(pos = pos + 1)
                                return@showBannerAd
                            }
                            getString(R.string.app_lovin).lowercase() -> {
                                if (adMob.appLovin?.isBannerAds == true) {
                                    val bannerID = adMob.appLovin?.appLovinBannerID ?: ""
                                    if (!(bannerID.isNullOrEmpty())) {
                                        loadAppLovinBanner(bannerID, adsId, pos + 1)
                                        return@showBannerAd
                                    }
                                }
                                showBannerAd(pos = pos + 1)
                            }
                            getString(R.string.facebook_ads).lowercase() -> {
                                if (adMob.facebookAds?.isBannerAds == true) {
                                    val bannerID = adMob.facebookAds?.bannerId ?: ""
                                    if (!(bannerID.isNullOrEmpty())) {
                                        loadFacebookBanner(bannerID, adsId, pos + 1)
                                        return@showBannerAd
                                    }
                                }
                                showBannerAd(pos = pos + 1)
                            }
                            getString(R.string.custom_ads).lowercase() -> {
                                adMob.customAds?.customBannerImage?.let { /*loadCustomBanner(it, pos)*/ }
                                    ?: kotlin.run {
                                        showBannerAd(pos + 1)
                                    }
                            }
                            else -> {
                                showBannerAd(pos = pos + 1)
                                return@showBannerAd
                            }
                        }
                    } else {
                        it.visibility = View.GONE
                    }
                } else {
                    it.visibility = View.GONE
                }
            }
        }
    }

    fun showNativeAd() {
        nativeContainer()?.let {
            var adsId = CommonAds.nativeAds.size
            if (adsId > 0) {
                try {
                    adsId =
                        CommonAds.nativeAds.indexOfFirst { intraAds -> (intraAds?.isRequested != true) && (intraAds?.isLoaded == true) && (intraAds?.isShowing != true) }
                } catch (exp: Exception) {
                    try {
                        adsId = CommonAds.nativeAds.indexOfFirst { intraAds -> intraAds == null }
                    } catch (exp: Exception) {
                    }
                }
            }

            if (adsId < 0) {
                adsId = CommonAds.nativeAds.size
            }

            loadNativeAds(adsId, pos = 0, onAdLoadedCallback = { index ->
                CommonAds.nativeAds[index]?.let { nativeAds ->
                    if (nativeAds.isLoaded) {
                        if (!nativeAds.isShowing) {
                            nativeAds.isShowing = true
                            try {
                                it.removeAllViews()
                            } catch (exp: Exception) {
                            } finally {
                                it.addView(nativeAds.show(this))
                            }
                        }
                    }
                }
            })
        }
    }

    fun showIntraAds(onShowcallback: () -> Unit?) {

        // Show Progress Dialog

        getAdSetting()?.let { _settings ->
            _settings.adSetting?.intraAdClickCount =
                (_settings.adSetting?.intraAdClickCount ?: 0) + 1
            var adsId = CommonAds.intraAds.size
            if (adsId > 0) {
                try {
                    adsId =
                        CommonAds.intraAds.indexOfFirst { intraAds -> (intraAds?.isRequested != true) && (intraAds?.isLoaded == true) && (intraAds?.isShowing != true) }
                } catch (exp: Exception) {
                    try {
                        adsId = CommonAds.intraAds.indexOfFirst { intraAds -> intraAds == null }
                    } catch (exp: Exception) {
                    }
                }
            }

            if (adsId < 0) {
                adsId = CommonAds.intraAds.size
            }

            loadIntraAds(adsId, onAdLoadedCallback = { index ->
                // Dismiss Dialog
                CommonAds.intraAds[index]?.let {
                    if (it.isLoaded) {
                        if (!it.isShowing) {
                            it.isShowing = true
                            it.show(this, object : AdShowCallback {
                                override fun onAdDismiss() {
                                    CommonAds.intraAds[index]?.dispose()
                                    CommonAds.intraAds[index] = null
                                    CommonAds.intraAds.removeAt(index)
                                    CommonAds.interestialAdTimeInterval =
                                        Calendar.getInstance().timeInMillis

                                    if (onShowcallback != null) {
                                        onShowcallback()
                                    }

                                    loadIntraAds(adsId)
                                }

                                override fun onAdFailedToShow() {

                                }

                                override fun onAdImpression() {}
                            })
                        }
                    }
                } ?: kotlin.run {
                    if (onShowcallback != null) {
                        onShowcallback()
                    }
                }
            })
            return@showIntraAds
        }

        if (onShowcallback != null) {
            onShowcallback()
        }
    }

    fun showRewardAds(onShowcallback: () -> Unit?) {

        // Show Progress Dialog

        getAdSetting()?.let { _settings ->

            var adsId = CommonAds.rewardAds.size
            if (adsId > 0) {
                try {
                    adsId =
                        CommonAds.rewardAds.indexOfFirst { intraAds -> (intraAds?.isRequested != true) && (intraAds?.isLoaded == true) && (intraAds?.isShowing != true) }
                } catch (exp: Exception) {
                    try {
                        adsId = CommonAds.rewardAds.indexOfFirst { intraAds -> intraAds == null }
                    } catch (exp: Exception) {
                    }
                }
            }

            if (adsId < 0) {
                adsId = CommonAds.rewardAds.size
            }

            loadRewardAds(adsId, onAdLoadedCallback = { index ->
                // Dismiss Dialog
                CommonAds.rewardAds[index]?.let {
                    if (it.isLoaded) {
                        if (!it.isShowing) {
                            it.isShowing = true
                            it.show(this, object : AdShowCallback {
                                override fun onAdDismiss() {
                                    val isVideoRewarded =
                                        if (CommonAds.rewardAds[index] is RewardAd) {
                                            ((CommonAds.rewardAds[index] as RewardAd).isVideoRewarded)
                                        } else if (CommonAds.rewardAds[index] is AppLovinRewardAd) {
                                            ((CommonAds.rewardAds[index] as AppLovinRewardAd).isVideoRewarded)
                                        }  else if (CommonAds.rewardAds[index] is FacebookRewardAd) {
                                            ((CommonAds.rewardAds[index] as FacebookRewardAd).isVideoRewarded)
                                        } else {
                                            false
                                        }

                                    CommonAds.rewardAds[index]?.dispose()
                                    CommonAds.rewardAds[index] = null
                                    CommonAds.rewardAds.removeAt(index)

                                    if (onShowcallback != null && isVideoRewarded) {
                                        onShowcallback()
                                    }
                                }

                                override fun onAdFailedToShow() {}

                                override fun onAdImpression() {}
                            })
                        }
                    }
                } ?: kotlin.run {
                    if (onShowcallback != null) {
                        onShowcallback()
                    }
                }
            })
            return@showRewardAds
        }

        if (onShowcallback != null) {
            onShowcallback()
        }
    }

    private fun loadBanner(bannerId: String, adsId: Int, pos: Int) {
        bannerContainer()?.post {
            try {
                if (CommonAds.bannerAds[adsId] == null) {
                    CommonAds.bannerAds[adsId] = BannerAd(bannerId)
                }
            } catch (e: Exception) {
                CommonAds.bannerAds.add(BannerAd(bannerId))
            }

            CommonAds.bannerAds[adsId]?.let {
                if (it.listener == null) {
                    it.listener = object : AdLoadCallback {
                        override fun onAdLoaded(ads: Ads) {
                            Log.e("onAdLoaded: ", "Banner ------------------- ")
                            CommonAds.bannerAds[adsId]?.let {
                                it.isRequested = false
                                it.isLoaded = true
                            }
                            try {
                                bannerContainer()?.removeAllViews()
                            } catch (exp: Exception) {
                            } finally {
                                if (ads is BannerAd) {
                                    bannerContainer()?.addView(ads.getBannerAd())
                                }

                                if (ads is AdMobBannerAd) {
                                    bannerContainer()?.addView(ads.getAbMobBannerAd())
                                }

                                if (ads is AppLovinBannerAd) {
                                    bannerContainer()?.addView(ads.getBannerAd())
                                }

                                if (ads is FacebookBannerAd) {
                                    bannerContainer()?.addView(ads.getBannerAd())
                                }
                            }
                        }

                        override fun onAdLoadError() {
                            CommonAds.bannerAds[adsId]?.let {
                                it.dispose()
                            }
                            CommonAds.bannerAds[adsId] = null
                            showBannerAd(pos + 1)
                        }

                    }
                }
                if (!it.isRequested) {
                    if (!it.isLoaded) {
                        it.isRequested = true
                        if (it is AppLovinBannerAd) {
                            it.load(this@BaseAdActivity)
                        } else {
                            it.load(this.applicationContext)
                        }
                    }
                }

            }
        }
    }

    private fun loadAdMobBanner(bannerId: String, adsId: Int, pos: Int) {
        bannerContainer()?.post {
            try {
                if (CommonAds.bannerAds[adsId] == null) {
                    CommonAds.bannerAds[adsId] = AdMobBannerAd(bannerId)
                }
            } catch (e: Exception) {
                CommonAds.bannerAds.add(AdMobBannerAd(bannerId))
            }

            loadBanner(bannerId, adsId, pos)
        }
    }

    private fun loadAppLovinBanner(bannerId: String, adsId: Int, pos: Int) {
        bannerContainer()?.post {
            try {
                if (CommonAds.bannerAds[adsId] == null) {
                    CommonAds.bannerAds[adsId] = AppLovinBannerAd(bannerId)
                }
            } catch (e: Exception) {
                CommonAds.bannerAds.add(AppLovinBannerAd(bannerId))
            }

            loadBanner(bannerId, adsId, pos)
        }
    }

    private fun loadFacebookBanner(bannerId: String, adsId: Int, pos: Int) {
        bannerContainer()?.post {
            try {
                if (CommonAds.bannerAds[adsId] == null) {
                    CommonAds.bannerAds[adsId] = FacebookBannerAd(bannerId)
                }
            } catch (e: Exception) {
                CommonAds.bannerAds.add(FacebookBannerAd(bannerId))
            }

            loadBanner(bannerId, adsId, pos)
        }
    }

    private fun loadCustomBanner(customAds: ArrayList<String>, clickableUrl: String?, pos: Int) {
        if (customAds.size > 0) {
            var random_index = Random().nextInt(customAds.size)

            if (random_index >= customAds.size) {
                random_index = customAds.size - 1
            }

            loadCustomBanner(
                customAds[random_index],
                clickableUrl ?: "",
                pos
            )
        } else {
            showBannerAd(pos + 1)
        }
    }

    private fun loadCustomBanner(imageUrl: String, clickableUrl: String, pos: Int) {
        if (imageUrl.isNullOrEmpty() || clickableUrl.isNullOrEmpty()) {
            showBannerAd(pos + 1)
            return
        }

        bannerContainer()?.post {
            /*val customBannerAdView = CustomBannerAdsBinding.inflate(layoutInflater)
            Glide.with(customBannerAdView.root.context).load(imageUrl).listener(object :
                RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    try {
                        AdContainerView()?.removeAllViews()
                    } catch (e: Exception) {
                    }
                    bannerAdSetting(pos + 1)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    try {
                        AdContainerView()?.removeAllViews()
                    } catch (e: Exception) {
                    }
                    AdContainerView()?.addView(customBannerAdView.root)
                    customBannerAdView.ivImage.setImageDrawable(resource)
                    customBannerAdView.ivImage.setOnClickListener {
                        try {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(clickableUrl))
                            browserIntent.setPackage("com.android.chrome")
                            startActivity(browserIntent)
                        } catch (e: Exception) {
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(clickableUrl))
                            startActivity(browserIntent)
                        }
                    }
                    return true
                }
            }).into(customBannerAdView.ivImage)*/
        }
    }

    private fun loadNativeAds(
        adsId: Int,
        pos: Int = 0,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        nativeContainer()?.let {
            getAdSetting()?.let { adMob ->
                if ((adMob.adsSequence?.size ?: 0) > pos) {
                    if (curAppVersionCode() < (adMob.adSetting?.appVersionCode ?: 0)) {
                        when (adMob.adsSequence?.get(pos)?.lowercase() ?: false) {
                            getString(R.string.admanager).lowercase(),
                            getString(R.string.google_ads).lowercase() -> {
                                if (adMob.googleAds?.isNativeAds == true) {
                                    val nativeID =
                                        adMob.googleAds?.nativeVideoId ?: (adMob.googleAds?.nativeId
                                            ?: "")
                                    if (!(nativeID.isNullOrEmpty())) {

                                        if (getString(R.string.admanager).lowercase().equals(
                                                adMob.adsSequence?.get(
                                                    pos
                                                )?.lowercase()
                                            )
                                        ) {
                                            loadAdMobNative(
                                                nativeID,
                                                adsId,
                                                pos,
                                                onAdLoadedCallback
                                            )
                                            return@loadNativeAds
                                        }

                                        loadNative(nativeID, adsId, pos, onAdLoadedCallback)
                                        return@loadNativeAds
                                    }
                                }
                                loadNativeAds(adsId, pos = pos + 1, onAdLoadedCallback)
                                return@loadNativeAds
                            }
                            getString(R.string.app_lovin).lowercase() -> {
                                if (adMob.appLovin?.isNativeAds == true) {
                                    val nativeID = adMob.appLovin?.appLovinNativeIDs?.manual
                                        ?: (adMob.appLovin?.appLovinNativeIDs?.medium
                                            ?: (adMob.appLovin?.appLovinNativeIDs?.small ?: ""))
                                    if (!(nativeID.isNullOrEmpty())) {
                                        loadAppLovinNative(nativeID, adsId, pos, onAdLoadedCallback)
                                        return@loadNativeAds
                                    }
                                }
                                loadNativeAds(adsId, pos = pos + 1, onAdLoadedCallback)
                                return@loadNativeAds
                            }
                            getString(R.string.facebook_ads).lowercase() -> {
                                if (adMob.facebookAds?.isNativeAds == true) {
                                    val nativeID = adMob.facebookAds?.nativeId ?: ""
                                    if (!(nativeID.isNullOrEmpty())) {
                                        loadFacebookNative(nativeID, adsId, pos, onAdLoadedCallback)
                                        return@loadNativeAds
                                    }
                                }
                                loadNativeAds(adsId, pos = pos + 1, onAdLoadedCallback)
                                return@loadNativeAds
                            }
                            /*getString(R.string.custom_ads).lowercase() -> {
                                adMob.customAds?.customBannerImage?.let { loadCustomBanner(it, pos) }
                                    ?: kotlin.run {
                                        showBannerAd(pos + 1)
                                    }
                            }*/
                            else -> {
                                loadNativeAds(adsId, pos = pos + 1, onAdLoadedCallback)
                                return@loadNativeAds
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadNative(
        nativeId: String,
        adsId: Int,
        pos: Int,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        nativeContainer()?.let {
            try {
                if (CommonAds.nativeAds[adsId] == null) {
                    CommonAds.nativeAds[adsId] = NativeAd(nativeId, BaseNativeAd(layoutInflater))
                }
            } catch (e: Exception) {
                CommonAds.nativeAds.add(NativeAd(nativeId, BaseNativeAd(layoutInflater)))
            }

            CommonAds.nativeAds[adsId]?.let { nativeAds ->
                if (nativeAds.listener == null) {
                    nativeAds.listener = object : AdLoadCallback {
                        override fun onAdLoaded(ads: Ads) {
                            Log.e("onAdLoaded: ", "Banner ------------------- ")
                            CommonAds.nativeAds[adsId]?.let {
                                it.isRequested = false
                                it.isLoaded = true
                            }
                            if (onAdLoadedCallback != null) {
                                Log.e("onAdLoaded: ", " ------------------- Callback")
                                onAdLoadedCallback(adsId)
                            }
                        }

                        override fun onAdLoadError() {
                            CommonAds.nativeAds[adsId]?.let {
                                it.dispose()
                            }
                            CommonAds.nativeAds[adsId] = null
                            loadNativeAds(
                                adsId,
                                pos = pos + 1, onAdLoadedCallback = onAdLoadedCallback
                            )
                        }

                    }
                }
                if (!nativeAds.isRequested) {
                    if (!nativeAds.isLoaded) {
                        nativeAds.isRequested = true
                        nativeAds.load(this@BaseAdActivity)
                    } else {
                        if (onAdLoadedCallback != null) {
                            onAdLoadedCallback(adsId)
                        }
                    }
                }
            }
        }
    }

    private fun loadAdMobNative(
        nativeId: String,
        adsId: Int,
        pos: Int,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        nativeContainer()?.let {
            try {
                if (CommonAds.nativeAds[adsId] == null) {
                    CommonAds.nativeAds[adsId] = NativeAd(
                        nativeId,
                        BaseNativeAd(layoutInflater),
                        adRequest = AdManagerAdRequest.Builder().build()
                    )
                }
            } catch (e: Exception) {
                CommonAds.nativeAds.add(
                    NativeAd(
                        nativeId,
                        BaseNativeAd(layoutInflater),
                        adRequest = AdManagerAdRequest.Builder().build()
                    )
                )
            }

            loadNative(nativeId, adsId, pos, onAdLoadedCallback)
        }
    }

    private fun loadAppLovinNative(
        nativeId: String,
        adsId: Int,
        pos: Int,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        nativeContainer()?.let {
            try {
                if (CommonAds.nativeAds[adsId] == null) {
                    CommonAds.nativeAds[adsId] =
                        AppLovinNativeAd(nativeId, BaseNativeAd(layoutInflater))
                }
            } catch (e: Exception) {
                CommonAds.nativeAds.add(AppLovinNativeAd(nativeId, BaseNativeAd(layoutInflater)))
            }

            loadNative(nativeId, adsId, pos, onAdLoadedCallback)
        }
    }

    private fun loadFacebookNative(
        nativeId: String,
        adsId: Int,
        pos: Int,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        nativeContainer()?.let {
            try {
                if (CommonAds.nativeAds[adsId] == null) {
                    CommonAds.nativeAds[adsId] =
                        FacebookNativeAd(nativeId, BaseNativeAd(layoutInflater))
                }
            } catch (e: Exception) {
                CommonAds.nativeAds.add(FacebookNativeAd(nativeId, BaseNativeAd(layoutInflater)))
            }

            loadNative(nativeId, adsId, pos, onAdLoadedCallback)
        }
    }

    /** Called when returning to the activity */
    override fun onResume() {
        super.onResume()
    }

    /** Called before the activity is destroyed  */
    override fun onDestroy() {
        super.onDestroy()
    }

    private fun loadRewardAds(
        adsId: Int,
        pos: Int = 0,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        getAdSetting()?.let { _settings ->
            val adsSize = (_settings.adsSequence?.size ?: 0)
            if (adsSize > 0 && adsSize > pos) {
                if (curAppVersionCode() < (_settings.adSetting?.appVersionCode ?: 0)) {
                    Log.e(
                        "loadRewardAdNow: ",
                        "rewardAdsSequence Size == ${_settings.adsSequence?.size}            pos == $pos       counter == ${_settings.adSetting?.intraAdClickCount}    intraIntervalClick == ${_settings.adSetting?.intraAdClickInterval}      intraIntervalTime == ${
                            ((_settings.adSetting?.intraAdTimeInterval
                                ?: 0) * 1000)
                        }"
                    )

                    when (_settings.adsSequence?.get(pos)) {
                        getString(R.string.google_ads),
                        getString(R.string.admanager) -> {
                            if ((_settings.googleAds?.isRewaredAds == true) &&
                                (_settings.googleAds?.rewaredId ?: "").isNotEmpty()

                            ) {
                                if (getString(R.string.admanager).equals(
                                        _settings.adsSequence?.get(
                                            pos
                                        )
                                    )
                                ) {
                                    _createAdManagerRewardAd(
                                        adsId,
                                        (_settings.googleAds?.rewaredId ?: ""),
                                        pos,
                                        onAdLoadedCallback
                                    )
                                    return@loadRewardAds
                                }

                                _createRewardAd(
                                    adsId,
                                    (_settings.googleAds?.rewaredId ?: ""),
                                    pos,
                                    onAdLoadedCallback
                                )
                                return@loadRewardAds
                            }
                            loadRewardAds(adsId, pos + 1, onAdLoadedCallback)
                            return@loadRewardAds
                        }
                        getString(R.string.app_lovin) -> {
                            if ((_settings.appLovin?.isRewaredAds == true) &&
                                (_settings.appLovin?.rewaredId ?: "").isNotEmpty()

                            ) {
                                _createAppLovinRewardAd(
                                    adsId,
                                    (_settings.appLovin?.rewaredId ?: ""),
                                    pos,
                                    onAdLoadedCallback
                                )
                                return@loadRewardAds
                            }
                            loadRewardAds(adsId, pos + 1, onAdLoadedCallback)
                            return@loadRewardAds
                        }
                        getString(R.string.facebook_ads) -> {
                            if ((_settings.facebookAds?.isRewaredAds == true) &&
                                (_settings.facebookAds?.rewaredId ?: "").isNotEmpty()
                            ) {
                                _createFacebookRewardAd(
                                    adsId,
                                    (_settings.facebookAds?.rewaredId ?: ""),
                                    pos + 1,
                                    onAdLoadedCallback
                                )
                                return@loadRewardAds
                            }
                            loadRewardAds(adsId, pos + 1, onAdLoadedCallback)
                            return@loadRewardAds
                        }

                        else -> {
                            loadRewardAds(adsId, pos + 1, onAdLoadedCallback)
                            return@loadRewardAds
                        }
                    }
                }
            }
        }

        if (onAdLoadedCallback != null) {
            onAdLoadedCallback(adsId)
        }
    }

    private fun _createRewardAd(
        adsId: Int,
        adUnitId: String,
        pos: Int = 0,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        try {
            if (CommonAds.rewardAds[adsId] == null) {
                CommonAds.rewardAds[adsId] = RewardAd(adUnitId, AdRequest.Builder().build())
            }
        } catch (e: Exception) {
            CommonAds.rewardAds.add(RewardAd(adUnitId, AdRequest.Builder().build()))
        }

        CommonAds.rewardAds[adsId]?.let {
            if (it.listener == null) {
                it.listener = object : AdLoadCallback {
                    override fun onAdLoaded(ads: Ads) {
                        Log.e("onAdLoaded: ", " ------------------- ")
                        CommonAds.rewardAds[adsId]?.let {
                            it.isRequested = false
                            it.isLoaded = true
                        }

                        if (onAdLoadedCallback != null) {
                            Log.e("onAdLoaded: ", " ------------------- Callback")
                            onAdLoadedCallback(adsId)
                        }
                    }

                    override fun onAdLoadError() {
                        CommonAds.rewardAds[adsId]?.let {
                            it.dispose()
                        }
                        CommonAds.rewardAds[adsId] = null
                        loadRewardAds(
                            adsId,
                            pos + 1,
                            onAdLoadedCallback
                        )
                    }

                }
            }
            if (!it.isRequested) {
                if (!it.isLoaded) {
                    it.isRequested = true
                    if (it is AppLovinRewardAd) {
                        it.load(this@BaseAdActivity)
                    } else {
                        it.load(this.applicationContext)
                    }
                } else {
                    if (onAdLoadedCallback != null) {
                        onAdLoadedCallback(adsId)
                    }
                }
            }
        }

    }

    private fun _createAdManagerRewardAd(
        adsId: Int,
        adUnitId: String,
        pos: Int = 0,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null,

        ) {
        try {
            if (CommonAds.rewardAds[adsId] == null) {
                CommonAds.rewardAds[adsId] =
                    RewardAd(adUnitId, AdManagerAdRequest.Builder().build())
            }
        } catch (e: Exception) {
            CommonAds.rewardAds.add(RewardAd(adUnitId, AdManagerAdRequest.Builder().build()))
        }

        _createRewardAd(adsId, adUnitId, pos, onAdLoadedCallback)
    }


    private fun _createAppLovinRewardAd(
        adsId: Int,
        adUnitId: String,
        pos: Int = 0,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        try {
            if (CommonAds.rewardAds[adsId] == null) {
                CommonAds.rewardAds[adsId] = AppLovinRewardAd(adUnitId)
            }
        } catch (e: Exception) {
            CommonAds.rewardAds.add(AppLovinRewardAd(adUnitId))
        }
        _createRewardAd(adsId, adUnitId, pos, onAdLoadedCallback)
    }

    private fun _createFacebookRewardAd(
        adsId: Int,
        adUnitId: String,
        pos: Int = 0,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        try {
            if (CommonAds.rewardAds[adsId] == null) {
                CommonAds.rewardAds[adsId] = FacebookRewardAd(adUnitId)
            }
        } catch (e: Exception) {
            CommonAds.rewardAds.add(FacebookRewardAd(adUnitId))
        }

        _createRewardAd(adsId, adUnitId, pos, onAdLoadedCallback)
    }

    private fun loadIntraAds(
        adsId: Int,
        pos: Int = 0,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        getAdSetting()?.let { _settings ->
            val adsSize = (_settings.adsSequence?.size ?: 0)
            if (adsSize > 0 && adsSize > pos) {
                if (curAppVersionCode() < (_settings.adSetting?.appVersionCode ?: 0)) {
                    Log.e(
                        "loadInterstitialAdNw: ",
                        "intraAdsSequence Size == ${_settings.adsSequence?.size}            pos == $pos       counter == ${_settings.adSetting?.intraAdClickCount}    intraIntervalClick == ${_settings.adSetting?.intraAdClickInterval}      intraIntervalTime == ${
                            ((_settings.adSetting?.intraAdTimeInterval
                                ?: 0) * 1000)
                        }"
                    )

                    val isInterestial = ((_settings.adSetting?.intraAdClickInterval ?: 0) >
                            0 &&
                            (_settings.adSetting?.intraAdClickCount ?: 0) %
                            (_settings.adSetting?.intraAdClickInterval ?: 0) ==
                            0 &&
                            ((CommonAds.interestialAdTimeInterval +
                                    ((_settings.adSetting?.intraAdTimeInterval ?: 0) *
                                            1000)) <=
                                    Calendar.getInstance().timeInMillis ||
                                    CommonAds.interestialAdTimeInterval == 0L))

                    when (_settings.adsSequence?.get(pos)) {
                        getString(R.string.google_ads),
                        getString(R.string.admanager) -> {
                            if ((((_settings.googleAds?.intraAdClickInterval ?: 0) <= 0 &&
                                        isInterestial) ||
                                        ((_settings.googleAds?.intraAdClickInterval ?: 0) > 0 &&
                                                ((_settings.adSetting?.intraAdClickCount ?: 0) %
                                                        (_settings.googleAds?.intraAdClickInterval
                                                            ?: 0) ==
                                                        0)))
                                && ((_settings.googleAds?.isInterstitialAds == true) &&
                                        (_settings.googleAds?.interestialId ?: "").isNotEmpty()
                                        )
                            ) {
                                if (getString(R.string.admanager).equals(
                                        _settings.adsSequence?.get(
                                            pos
                                        )
                                    )
                                ) {
                                    _createAdManagerInterstitialAd(
                                        adsId,
                                        (_settings.googleAds?.interestialId ?: ""),
                                        pos,
                                        onAdLoadedCallback
                                    )
                                    return@loadIntraAds
                                }
                                _createInterstitialAd(
                                    adsId,
                                    (_settings.googleAds?.interestialId ?: ""),
                                    pos,
                                    onAdLoadedCallback
                                )
                                return@loadIntraAds
                            }
                            loadIntraAds(adsId, pos + 1, onAdLoadedCallback)
                            return@loadIntraAds
                        }
                        getString(R.string.app_lovin) -> {
                            if ((isInterestial || (_settings.appLovin?.isUnlimitedAds == true)) &&
                                ((_settings.appLovin?.isInterstitialAds == true) &&
                                        (_settings.appLovin?.interestialId ?: "").isNotEmpty()
                                        )
                            ) {
                                _createAppLovinInterstitialAd(
                                    adsId,
                                    (_settings.appLovin?.interestialId ?: ""),
                                    pos,
                                    onAdLoadedCallback
                                );
                                return@loadIntraAds
                            }
                            loadIntraAds(adsId, pos + 1, onAdLoadedCallback)
                            return@loadIntraAds
                        }
                        getString(R.string.facebook_ads) -> {
                            if ((((_settings.facebookAds?.intraAdClickInterval ?: 0) <= 0 &&
                                        isInterestial) ||
                                        ((_settings.facebookAds?.intraAdClickInterval ?: 0) > 0 &&
                                                ((_settings.adSetting?.intraAdClickCount ?: 0) %
                                                        (_settings.facebookAds?.intraAdClickInterval
                                                            ?: 0) ==
                                                        0))) && ((_settings.facebookAds?.isInterstitialAds == true) &&
                                        (_settings.facebookAds?.interestialId ?: "").isNotEmpty())
                            ) {
                                _createFacebookInterstitialAd(
                                    adsId,
                                    (_settings.facebookAds?.interestialId ?: ""),
                                    pos,
                                    onAdLoadedCallback
                                )
                                return@loadIntraAds
                            }
                            loadIntraAds(adsId, pos + 1, onAdLoadedCallback)
                            return@loadIntraAds
                        }

                        else -> {
                            loadIntraAds(adsId, pos + 1, onAdLoadedCallback)
                            return@loadIntraAds
                        }

                    }

                }
            }
        }

        if (onAdLoadedCallback != null) {
            onAdLoadedCallback(adsId)
        }
    }

    private fun _createInterstitialAd(
        adsId: Int,
        adUnitId: String,
        pos: Int = 0,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        try {
            if (CommonAds.intraAds[adsId] == null) {
                CommonAds.intraAds[adsId] = InterstitialAd(adUnitId)
            }
        } catch (e: Exception) {
            CommonAds.intraAds.add(InterstitialAd(adUnitId))
        }

        CommonAds.intraAds[adsId]?.let {
            if (it.listener == null) {
                it.listener = object : AdLoadCallback {
                    override fun onAdLoaded(ads: Ads) {
                        Log.e("onAdLoaded: ", " ------------------- ")
                        CommonAds.intraAds[adsId]?.let {
                            it.isRequested = false
                            it.isLoaded = true
                        }
                        if (onAdLoadedCallback != null) {
                            Log.e("onAdLoaded: ", " ------------------- Callback")
                            onAdLoadedCallback(adsId)
                        }
                    }

                    override fun onAdLoadError() {
                        CommonAds.intraAds[adsId]?.let {
                            it.dispose()
                        }
                        CommonAds.intraAds[adsId] = null
                        loadIntraAds(
                            adsId,
                            pos + 1,
                            onAdLoadedCallback
                        )
                    }

                }
            }
            if (!it.isRequested) {
                if (!it.isLoaded) {
                    it.isRequested = true
                    if (it is AppLovinInterstitialAd) {
                        it.load(this@BaseAdActivity)
                    } else {
                        it.load(this.applicationContext)
                    }
                } else {
                    if (onAdLoadedCallback != null) {
                        onAdLoadedCallback(adsId)
                    }
                }
            }
        }

    }

    private fun _createAdManagerInterstitialAd(
        adsId: Int,
        adUnitId: String,
        pos: Int = 0,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        try {
            if (CommonAds.intraAds[adsId] == null) {
                CommonAds.intraAds[adsId] = AdMobInterstitialAd(adUnitId)
            }
        } catch (e: Exception) {
            CommonAds.intraAds.add(AdMobInterstitialAd(adUnitId))
        }

        _createInterstitialAd(adsId, adUnitId, pos, onAdLoadedCallback)
    }

    private fun _createAppLovinInterstitialAd(
        adsId: Int,
        adUnitId: String,
        pos: Int = 0,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        try {
            if (CommonAds.intraAds[adsId] == null) {
                CommonAds.intraAds[adsId] = AppLovinInterstitialAd(adUnitId)
            }
        } catch (e: Exception) {
            CommonAds.intraAds.add(AppLovinInterstitialAd(adUnitId))
        }

        _createInterstitialAd(adsId, adUnitId, pos, onAdLoadedCallback)
    }

    private fun _createFacebookInterstitialAd(
        adsId: Int,
        adUnitId: String,
        pos: Int = 0,
        onAdLoadedCallback: ((adsId: Int) -> Unit)? = null
    ) {
        try {
            if (CommonAds.intraAds[adsId] == null) {
                CommonAds.intraAds[adsId] = FacebookInterstitialAd(adUnitId)
            }
        } catch (e: Exception) {
            CommonAds.intraAds.add(FacebookInterstitialAd(adUnitId))
        }

        _createInterstitialAd(adsId, adUnitId, pos, onAdLoadedCallback)
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}
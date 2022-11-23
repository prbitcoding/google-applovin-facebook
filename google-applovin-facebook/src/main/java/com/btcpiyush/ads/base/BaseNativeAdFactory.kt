package com.btcpiyush.ads.base

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.facebook.ads.NativeAdLayout
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

interface BaseNativeAdFactory {
    /**
     * Creates a [NativeAdView] with a [ ].
     *
     * @param nativeAd      Ad information used to create a [                      ]
     * @param customOptions Used to pass additional custom options to create the [                      ]. Nullable.
     * @return a [NativeAdView] that is overlaid on top of
     * the FlutterView
     */
    fun createNativeAd(nativeAd: NativeAd?, customOptions: Map<String?, Any?>?): NativeAdView?

    fun createMediumNativeAd(nativeAd: NativeAd?, customOptions: Map<String?, Any?>?): NativeAdView?

    fun createFacebookMediumNativeAd(
        context: AppCompatActivity,
        nativeAd: com.facebook.ads.NativeAd?
    ): NativeAdLayout?

    fun createFacebookNativeAd(
        context: AppCompatActivity,
        nativeAd: com.facebook.ads.NativeAd?
    ): NativeAdLayout?

    fun createAppLovinNativeAd(context: AppCompatActivity): MaxNativeAdView?

    fun createAppLovinMediumNativeAd(context: AppCompatActivity): MaxNativeAdView?
}
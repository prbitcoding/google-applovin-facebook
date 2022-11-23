package com.btcpiyush.ads.base

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.btcpiyush.ads.R
import com.facebook.ads.AdOptionsView
import com.facebook.ads.NativeAdLayout
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import java.util.ArrayList

class BaseNativeAd(layoutInflater: LayoutInflater) : BaseNativeAdFactory {
    private val layoutInflater: LayoutInflater

    override fun createNativeAd(
        nativeAd: NativeAd?,
        customOptions: Map<String?, Any?>?
    ): NativeAdView? {
        val adView = layoutInflater.inflate(R.layout.native_full, null) as NativeAdView

        nativeAd?.let{ nativeAd ->
            // Set the media view.
            adView.mediaView = adView.findViewById<View>(R.id.media_view1) as MediaView

            // Set other ad assets.
            adView.headlineView = adView.findViewById(R.id.primary)
            adView.bodyView = adView.findViewById(R.id.body_text)
            adView.callToActionView = adView.findViewById(R.id.cta)
            adView.iconView = adView.findViewById(R.id.icon)
            //    adView.setPriceView(adView.findViewById(R.id.ad_price));
//    adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.storeView = adView.findViewById(R.id.body)
            //    adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

            // The headline and mediaContent are guaranteed to be in every NativeAd.
            (adView.headlineView as TextView?)?.text = nativeAd.headline
            if (nativeAd.mediaContent == null) {
                adView.mediaView?.visibility = View.GONE
            } else {
                adView.mediaView?.visibility = View.VISIBLE
                adView.mediaView?.mediaContent = nativeAd.mediaContent
            }

            // These assets aren't guaranteed to be in every NativeAd, so it's important to
            // check before trying to display them.
            if (nativeAd.body == null) {
                adView.bodyView?.visibility = View.INVISIBLE
            } else {
                adView.bodyView?.visibility = View.VISIBLE
                (adView.bodyView as TextView?)?.text = nativeAd.body
            }
            if (nativeAd.callToAction == null) {
                adView.callToActionView?.visibility = View.INVISIBLE
            } else {
                adView.callToActionView?.visibility = View.VISIBLE
                (adView.callToActionView as Button?)?.text = nativeAd.callToAction
            }
            if (nativeAd.icon == null) {
                adView.iconView?.visibility = View.GONE
            } else {
                (adView.iconView as ImageView?)?.setImageDrawable(
                    nativeAd.icon?.drawable
                )
                adView.iconView?.visibility = View.VISIBLE
            }

            /* if (nativeAd.getPrice() == null) {
          adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
          adView.getPriceView().setVisibility(View.VISIBLE);
          ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }*/if (nativeAd.store == null) {
            adView.storeView?.visibility = View.INVISIBLE
        } else {
            adView.storeView?.visibility = View.VISIBLE
            (adView.storeView as TextView?)?.text = nativeAd.store
        }

            /*if (nativeAd.getStarRating() == null) {
          adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
          ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
          adView.getStarRatingView().setVisibility(View.VISIBLE);
        }*/

            /* if (nativeAd.getAdvertiser() == null) {
          adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
          adView.getAdvertiserView().setVisibility(View.VISIBLE);
          ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
        }*/

            // This method tells the Google Mobile Ads SDK that you have finished populating your
            // native ad view with this native ad.
            adView.setNativeAd(nativeAd)
        }

        return adView
    }

    override fun createMediumNativeAd(
        nativeAd: NativeAd?,
        customOptions: Map<String?, Any?>?
    ): NativeAdView {
        val adView = layoutInflater.inflate(R.layout.native_medium, null) as NativeAdView
        nativeAd?.let { nativeAd ->
            // Set other ad assets.
            adView.headlineView = adView.findViewById(R.id.primary)
            adView.bodyView = adView.findViewById(R.id.body_text)
            adView.callToActionView = adView.findViewById(R.id.cta)
            adView.iconView = adView.findViewById(R.id.icon)
            adView.storeView = adView.findViewById(R.id.body)
            (adView.headlineView as TextView?)?.text = nativeAd.headline
            if (nativeAd.body == null) {
                adView.bodyView?.visibility = View.INVISIBLE
            } else {
                adView.bodyView?.visibility = View.VISIBLE
                (adView.bodyView as TextView?)?.setText(nativeAd.body)
            }
            if (nativeAd.callToAction == null) {
                adView.callToActionView?.visibility = View.INVISIBLE
            } else {
                adView.callToActionView?.visibility = View.VISIBLE
                (adView.callToActionView as Button?)?.text = nativeAd.callToAction
            }
            if (nativeAd.icon == null) {
                adView.iconView?.visibility = View.GONE
            } else {
                (adView.iconView as ImageView?)?.setImageDrawable(
                    nativeAd.icon?.drawable
                )
                adView.iconView?.visibility = View.VISIBLE
            }
            if (nativeAd.store == null) {
                adView.storeView?.visibility = View.INVISIBLE
            } else {
                adView.storeView?.visibility = View.VISIBLE
                (adView.storeView as TextView?)?.text = nativeAd.store
            }
            adView.setNativeAd(nativeAd)
        }

        return adView
    }

    override fun createFacebookMediumNativeAd(
        context: AppCompatActivity,
        nativeAd: com.facebook.ads.NativeAd?
    ): NativeAdLayout {
        val nativeAdLayout =
            layoutInflater.inflate(R.layout.native_facebook_ads_layout, null) as NativeAdLayout
        nativeAd?.let { nativeAd ->
            nativeAd.unregisterView()

            val inflater: LayoutInflater = LayoutInflater.from(context)
            // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
            val adView: LinearLayout =
                inflater.inflate(R.layout.native_facebook_ads, nativeAdLayout, false) as LinearLayout
            nativeAdLayout.addView(adView)

            // Add the AdOptionsView
            val adChoicesContainer: LinearLayout =
                adView.findViewById<LinearLayout>(R.id.ad_choices_container)
            val adOptionsView = AdOptionsView(context, nativeAd, nativeAdLayout)
            adChoicesContainer.removeAllViews()
            adChoicesContainer.addView(adOptionsView, 0)

            // Create native UI using the ad metadata.
            val nativeAdIcon: com.facebook.ads.MediaView =
                adView.findViewById<com.facebook.ads.MediaView>(R.id.native_ad_icon)
            val nativeAdTitle: TextView = adView.findViewById<TextView>(R.id.native_ad_title)
            val nativeAdMedia: com.facebook.ads.MediaView =
                adView.findViewById<com.facebook.ads.MediaView>(R.id.native_ad_media)
            val nativeAdSocialContext: TextView =
                adView.findViewById<TextView>(R.id.native_ad_social_context)
            val nativeAdBody: TextView = adView.findViewById<TextView>(R.id.native_ad_body)
            val sponsoredLabel: TextView = adView.findViewById<TextView>(R.id.native_ad_sponsored_label)
            val nativeAdCallToAction: Button =
                adView.findViewById<Button>(R.id.native_ad_call_to_action)
            nativeAdMedia.visibility = View.GONE
            // Set the Text.
            nativeAdTitle.text = nativeAd.advertiserName
            nativeAdBody.text = nativeAd.adBodyText
            nativeAdSocialContext.text = nativeAd.adSocialContext
            nativeAdCallToAction.visibility =
                if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
            nativeAdCallToAction.text = nativeAd.adCallToAction
            sponsoredLabel.text = nativeAd.sponsoredTranslation

            // Create a list of clickable views
            val clickableViews: MutableList<View> = ArrayList()
            clickableViews.add(nativeAdTitle)
            clickableViews.add(nativeAdCallToAction)

            // Register the Title and CTA button to listen for clicks.
            nativeAd.registerViewForInteraction(adView, nativeAdMedia, nativeAdIcon, clickableViews)
        }

        return nativeAdLayout
    }

    override fun createFacebookNativeAd(
        context: AppCompatActivity,
        nativeAd: com.facebook.ads.NativeAd?
    ): NativeAdLayout {
        val nativeAdLayout =
            layoutInflater.inflate(R.layout.native_facebook_ads_layout, null) as NativeAdLayout
        nativeAd?.let { nativeAd ->
            nativeAd.unregisterView()

            val inflater: LayoutInflater = LayoutInflater.from(context)
            // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
            val adView: LinearLayout =
                inflater.inflate(
                    R.layout.native_facebook_ads,
                    nativeAdLayout,
                    false
                ) as LinearLayout
            nativeAdLayout.addView(adView)

            // Add the AdOptionsView
            val adChoicesContainer: LinearLayout =
                adView.findViewById<LinearLayout>(R.id.ad_choices_container)
            val adOptionsView = AdOptionsView(context, nativeAd, nativeAdLayout)
            adChoicesContainer.removeAllViews()
            adChoicesContainer.addView(adOptionsView, 0)

            // Create native UI using the ad metadata.
            val nativeAdIcon: com.facebook.ads.MediaView =
                adView.findViewById<com.facebook.ads.MediaView>(R.id.native_ad_icon)
            val nativeAdTitle: TextView = adView.findViewById<TextView>(R.id.native_ad_title)
            val nativeAdMedia: com.facebook.ads.MediaView =
                adView.findViewById<com.facebook.ads.MediaView>(R.id.native_ad_media)
            val nativeAdSocialContext: TextView =
                adView.findViewById<TextView>(R.id.native_ad_social_context)
            val nativeAdBody: TextView = adView.findViewById<TextView>(R.id.native_ad_body)
            val sponsoredLabel: TextView =
                adView.findViewById<TextView>(R.id.native_ad_sponsored_label)
            val nativeAdCallToAction: Button =
                adView.findViewById<Button>(R.id.native_ad_call_to_action)

            // Set the Text.
            nativeAdTitle.text = nativeAd.advertiserName
            nativeAdBody.text = nativeAd.adBodyText
            nativeAdSocialContext.text = nativeAd.adSocialContext
            nativeAdCallToAction.visibility =
                if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
            nativeAdCallToAction.text = nativeAd.adCallToAction
            sponsoredLabel.text = nativeAd.sponsoredTranslation

            // Create a list of clickable views
            val clickableViews: MutableList<View> = ArrayList()
            clickableViews.add(nativeAdTitle)
            clickableViews.add(nativeAdCallToAction)

            // Register the Title and CTA button to listen for clicks.
            nativeAd.registerViewForInteraction(adView, nativeAdMedia, nativeAdIcon, clickableViews)
        }

        return nativeAdLayout
    }

    override fun createAppLovinNativeAd(context: AppCompatActivity): MaxNativeAdView {
        val binder: MaxNativeAdViewBinder =
            MaxNativeAdViewBinder.Builder(R.layout.native_full_template)
                .setTitleTextViewId(R.id.primary)
                .setBodyTextViewId(R.id.body_text)
                .setAdvertiserTextViewId(R.id.body)
                .setIconImageViewId(R.id.icon)
                .setMediaContentViewGroupId(R.id.media_view)
                .setCallToActionButtonId(R.id.cta)
                .build()
        return MaxNativeAdView(binder, context)
    }

    override fun createAppLovinMediumNativeAd(context: AppCompatActivity): MaxNativeAdView {
        val binder: MaxNativeAdViewBinder =
            MaxNativeAdViewBinder.Builder(R.layout.native_medium_template)
                .setTitleTextViewId(R.id.primary)
                .setBodyTextViewId(R.id.body_text)
                .setAdvertiserTextViewId(R.id.body)
                .setIconImageViewId(R.id.icon)
                .setCallToActionButtonId(R.id.cta)
                .build()
        return MaxNativeAdView(binder, context)
    }

    init {
        this.layoutInflater = layoutInflater
    }
}
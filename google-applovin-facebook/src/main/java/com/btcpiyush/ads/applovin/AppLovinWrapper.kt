// Copyright 2022 Bitcoding LLC
//
// See the License for the specific language governing permissions and
// limitations under the License.

package com.btcpiyush.ads.applovin

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.appharbr.sdk.configuration.AHSdkConfiguration
import com.appharbr.sdk.engine.AdBlockReason
import com.appharbr.sdk.engine.AppHarbr
import com.appharbr.sdk.engine.InitializationFailureReason
import com.appharbr.sdk.engine.adformat.AdFormat
import com.appharbr.sdk.engine.listeners.AHListener
import com.appharbr.sdk.engine.listeners.OnAppHarbrInitializationCompleteListener
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdk.SdkInitializationListener
import com.applovin.sdk.AppLovinSdkConfiguration
import com.applovin.sdk.AppLovinSdkSettings
import java.util.*

/** A wrapper around static methods in [AppLovin].  */
class AppLovinWrapper {

    private var _appLovinSDK: AppLovinSdk? = null
    private var _isGeoEdgeInitialized: Boolean = false

    /** Retrieve the sdk key.  */
    fun initializeSDK(
        context: Context?, appLovinSdkKey: String
    ) {
        var sdkKey = appLovinSdkKey
        Log.e("initializeSDK: ", sdkKey)
        if (TextUtils.isEmpty(sdkKey)) {
            Log.e(" = == == = = =", "initializeSDK: TextUtils is Empty")
            try {
                Log.e(" = == == = = =", "initializeSDK: context checking")
                context?.let {
                    Log.e(" = == == = = =", "initializeSDK: context is done")
                    val packageManager = it.packageManager
                    val packageName = it.packageName
                    val applicationInfo = packageManager.getApplicationInfo(
                        packageName,
                        PackageManager.GET_META_DATA
                    )
                    val metaData = applicationInfo.metaData
                    sdkKey = metaData.getString("applovin.sdk.key", "")
                }

            } catch (th: Throwable) {
                th.printStackTrace()
                Log.e(" = == == = = =", "initializeSDK: context is done " + th.message)
            }
        }
        try {
            Log.e(" = == == = = =", "SDK Key Start")
            _appLovinSDK = AppLovinSdk.getInstance(
                sdkKey,
                AppLovinSdkSettings(context),
                context
            )
            Log.e(" = == == = = =", "SDK Key Done ++" + _appLovinSDK?.sdkKey)
        } catch (e: Exception) {
            Log.e("initializeSDK: ", e.message.toString())
            _appLovinSDK = null
        }

    }

    /** Initializes the sdk. */
    fun initialize(
        listener: SdkInitializationListener
    ) {
        _appLovinSDK?.initializeSdk(listener)
    }

    /** Wrapper for setMediationProvider.  */
    fun setMediationProvider(provider: String) {
        _appLovinSDK?.mediationProvider = provider
    }

    /** Wrapper for setAppMuted.  */
    fun setAppMuted(muted: Boolean) {
        _appLovinSDK?.settings?.isMuted = muted
    }

    /** Wrapper for setVerboseLogging.  */
    fun setVerboseLogging(verboseLogging: Boolean) {
        _appLovinSDK?.settings?.setVerboseLogging(verboseLogging)
    }

    /** Wrapper for getRequestConfiguration.  */
    val requestConfiguration: AppLovinSdkConfiguration?
        get() = _appLovinSDK?.configuration

    fun getAppLovinSdk(): AppLovinSdk? {
        return _appLovinSDK
    }

    fun IsGEOEdgeSDKInitialized(): Boolean {
        return _isGeoEdgeInitialized
    }

    fun initGEOEdgeSDK(act: Context, geoedgeSdkKey: String) {
        try {
            if (geoedgeSdkKey.isNullOrEmpty()) {
                return@initGEOEdgeSDK
            }

            val ahSdkConfiguration : AHSdkConfiguration = AHSdkConfiguration.Builder(geoedgeSdkKey).build()
            AppHarbr.initialize(act,
                ahSdkConfiguration,
                object : OnAppHarbrInitializationCompleteListener {
                    override fun onSuccess() {
                        _isGeoEdgeInitialized = true
                        Log.e("LOG", "AppHarbr SDK Initialized Successfully")
                        //Toast.makeText(act, "AppHarbr SDK Initialized Successfully", Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(reason: InitializationFailureReason) {
                        _isGeoEdgeInitialized = false
                        Log.e("LOG", "AppHarbr SDK Initialization Failed: " + reason.readableHumanReason)
                        //Toast.makeText(act, "AppHarbr SDK Initialization Failed: " + reason.readableHumanReason, Toast.LENGTH_LONG).show()
                    }
                }
            )
        } catch (e: Exception) {

        }
    }

    fun getGEOSDKWrapper() : AHListener {
        return AHListener { view: Any?, unitId: String, adFormat: AdFormat?, reasons: Array<AdBlockReason?>? ->
            /*Toast.makeText(act, "AppHarbr - onAdBlocked for: $unitId, reason: " + Arrays.toString(
                reasons
            ), Toast.LENGTH_SHORT ).show()*/
            Log.e(
                "LOG",
                "AppHarbr - onAdBlocked for: $unitId, reason: " + Arrays.toString(
                    reasons
                )
            )
        }
    }
}
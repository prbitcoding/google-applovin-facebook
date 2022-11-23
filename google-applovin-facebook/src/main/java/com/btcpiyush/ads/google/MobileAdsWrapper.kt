// Copyright 2022 Bitcoding LLC
//
// See the License for the specific language governing permissions and
// limitations under the License.
package com.btcpiyush.ads.google

import android.content.Context
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.OnAdInspectorClosedListener

/** A wrapper around static methods in [MobileAds].  */
class MobileAdsWrapper {
    /** Initializes the sdk.  */
    fun initialize(
        context: Context, listener: OnInitializationCompleteListener
    ) {
        MobileAds.initialize(context, listener)
    }

    /** Wrapper for setAppMuted.  */
    fun setAppMuted(muted: Boolean) {
        MobileAds.setAppMuted(muted)
    }

    /** Wrapper for setAppVolume.  */
    fun setAppVolume(volume: Double) {
        MobileAds.setAppVolume(volume.toFloat())
    }

    /** Wrapper for disableMediationInitialization.  */
    fun disableMediationInitialization(context: Context) {
        MobileAds.disableMediationAdapterInitialization(context)
    }

    /** Wrapper for getVersionString.  */
    val versionString: String
        get() = MobileAds.getVersion().toString()

    /** Wrapper for getRequestConfiguration.  */
    val requestConfiguration: RequestConfiguration
        get() = MobileAds.getRequestConfiguration()

    /** Wrapper for openDebugMenu.  */
    fun openDebugMenu(context: Context?, adUnitId: String?) {
        MobileAds.openDebugMenu(context!!, adUnitId!!)
    }

    /** Open the ad inspector.  */
    fun openAdInspector(context: Context?, listener: OnAdInspectorClosedListener?) {
        MobileAds.openAdInspector(context!!, listener!!)
    }
}
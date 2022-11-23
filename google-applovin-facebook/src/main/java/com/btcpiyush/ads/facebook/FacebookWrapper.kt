// Copyright 2022 Bitcoding LLC
//
// See the License for the specific language governing permissions and
// limitations under the License.
package com.btcpiyush.ads.facebook

import android.content.Context
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.facebook.ads.BuildConfig
import com.google.android.gms.ads.MobileAds

/** A wrapper around static methods in [MobileAds].  */
class FacebookWrapper {
    /** Initializes the sdk.  */
    fun initialize(
        context: Context
    ) {
        if (BuildConfig.DEBUG) {
            AdSettings.turnOnSDKDebugger(context)
            AdSettings.isTestMode(context)
        }
        AudienceNetworkAds.initialize(context)
    }
}
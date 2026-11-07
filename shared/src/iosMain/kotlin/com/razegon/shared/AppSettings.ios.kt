package com.razegon.shared

import androidx.compose.runtime.Composable
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

@Composable
actual fun openAppSettings(): () -> Unit {
    return {
        val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)

        if (url != null) {
            UIApplication.sharedApplication.openURL(
                url = url,
                options = emptyMap<Any?, Any>(),
                completionHandler = null
            )
        }
    }
}
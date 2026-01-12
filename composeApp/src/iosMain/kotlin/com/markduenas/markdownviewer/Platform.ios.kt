package com.markduenas.markdownviewer

import platform.Foundation.NSBundle
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun getAppVersion(): String {
    val version = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String ?: "1.0"
    val build = NSBundle.mainBundle.infoDictionary?.get("CFBundleVersion") as? String ?: "1"
    return "$version ($build)"
}

object IOSInitialContentHolder {
    var initialContent: String? = null
    var initialFileName: String? = null
}

actual fun getInitialContent(): InitialContent? {
    val content = IOSInitialContentHolder.initialContent
    val fileName = IOSInitialContentHolder.initialFileName
    return if (content != null && fileName != null) {
        InitialContent(content, fileName)
    } else {
        null
    }
}

actual fun clearInitialContent() {
    IOSInitialContentHolder.initialContent = null
    IOSInitialContentHolder.initialFileName = null
}

actual fun openDeveloperApps() {
    // Open App Store developer page directly
    val urlString = "itms-apps://apps.apple.com/us/developer/mark-duenas/id1083533055"
    val url = platform.Foundation.NSURL.URLWithString(urlString)
    if (url != null) {
        platform.UIKit.UIApplication.sharedApplication.openURL(url, emptyMap<Any?, Any?>()) { _ -> }
    }
}
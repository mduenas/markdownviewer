package com.markduenas.markdownviewer

import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

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
    dispatch_async(dispatch_get_main_queue()) {
        val url = NSURL.URLWithString("https://apps.apple.com/us/developer/mark-duenas/id1083533055")
        if (url != null) {
            UIApplication.sharedApplication.openURL(url, emptyMap<Any?, Any?>()) { _ -> }
        }
    }
}
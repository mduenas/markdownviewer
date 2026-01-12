package com.markduenas.markdownviewer

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

object AppVersionHolder {
    var versionName: String = "1.0"
}

actual fun getAppVersion(): String = AppVersionHolder.versionName

actual fun getInitialContent(): InitialContent? {
    val content = IntentContentHolder.initialContent
    val fileName = IntentContentHolder.initialFileName
    return if (content != null && fileName != null) {
        InitialContent(content, fileName)
    } else {
        null
    }
}

actual fun clearInitialContent() {
    IntentContentHolder.initialContent = null
    IntentContentHolder.initialFileName = null
}
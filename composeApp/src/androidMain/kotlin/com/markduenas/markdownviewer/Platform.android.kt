package com.markduenas.markdownviewer

import android.content.Intent
import android.net.Uri
import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

object AppVersionHolder {
    var versionName: String = "1.0"
    var activityContext: android.app.Activity? = null
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

actual fun openDeveloperApps() {
    val uri = Uri.parse("https://play.google.com/store/apps/developer?id=Mark+Duenas")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    AppVersionHolder.activityContext?.startActivity(intent)
}
package com.markduenas.markdownviewer.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

actual object Analytics {
    private val firebaseAnalytics: FirebaseAnalytics by lazy { Firebase.analytics }

    actual fun logEvent(event: String, params: Map<String, String>) {
        firebaseAnalytics.logEvent(event) {
            params.forEach { (key, value) ->
                param(key, value)
            }
        }
    }

    actual fun logFileOpened(fileName: String, source: String) {
        val extension = fileName.substringAfterLast('.', "unknown")
        firebaseAnalytics.logEvent(AnalyticsEvents.FILE_OPENED) {
            param(AnalyticsParams.FILE_NAME, fileName.take(100))
            param(AnalyticsParams.FILE_EXTENSION, extension)
            param(AnalyticsParams.SOURCE, source)
        }
    }

    actual fun logUrlOpened(url: String) {
        firebaseAnalytics.logEvent(AnalyticsEvents.URL_OPENED) {
            param(AnalyticsParams.URL, url.take(100))
        }
    }

    actual fun logMermaidFullscreen() {
        firebaseAnalytics.logEvent(AnalyticsEvents.MERMAID_FULLSCREEN, null)
    }

    actual fun logRecentFileOpened(fileName: String) {
        firebaseAnalytics.logEvent(AnalyticsEvents.RECENT_FILE_OPENED) {
            param(AnalyticsParams.FILE_NAME, fileName.take(100))
        }
    }

    actual fun logAboutShown() {
        firebaseAnalytics.logEvent(AnalyticsEvents.ABOUT_SHOWN, null)
    }

    actual fun logMoreAppsClicked() {
        firebaseAnalytics.logEvent(AnalyticsEvents.MORE_APPS_CLICKED, null)
    }

    actual fun logFileClosed() {
        firebaseAnalytics.logEvent(AnalyticsEvents.FILE_CLOSED, null)
    }

    actual fun logError(errorType: String, errorMessage: String) {
        firebaseAnalytics.logEvent(AnalyticsEvents.ERROR_OCCURRED) {
            param(AnalyticsParams.ERROR_TYPE, errorType.take(100))
            param(AnalyticsParams.ERROR_MESSAGE, errorMessage.take(100))
        }
    }
}

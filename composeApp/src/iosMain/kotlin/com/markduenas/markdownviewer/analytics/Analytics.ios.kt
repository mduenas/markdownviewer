package com.markduenas.markdownviewer.analytics

// iOS Analytics implementation
// Note: Custom events are logged to console. Firebase auto-events still work.
// For full custom event support, CocoaPods interop configuration would be needed.

actual object Analytics {
    actual fun logEvent(event: String, params: Map<String, String>) {
        println("[Analytics] $event: $params")
    }

    actual fun logFileOpened(fileName: String, source: String) {
        println("[Analytics] ${AnalyticsEvents.FILE_OPENED}: fileName=$fileName, source=$source")
    }

    actual fun logUrlOpened(url: String) {
        println("[Analytics] ${AnalyticsEvents.URL_OPENED}: url=$url")
    }

    actual fun logMermaidFullscreen() {
        println("[Analytics] ${AnalyticsEvents.MERMAID_FULLSCREEN}")
    }

    actual fun logRecentFileOpened(fileName: String) {
        println("[Analytics] ${AnalyticsEvents.RECENT_FILE_OPENED}: fileName=$fileName")
    }

    actual fun logAboutShown() {
        println("[Analytics] ${AnalyticsEvents.ABOUT_SHOWN}")
    }

    actual fun logMoreAppsClicked() {
        println("[Analytics] ${AnalyticsEvents.MORE_APPS_CLICKED}")
    }

    actual fun logFileClosed() {
        println("[Analytics] ${AnalyticsEvents.FILE_CLOSED}")
    }

    actual fun logError(errorType: String, errorMessage: String) {
        println("[Analytics] ${AnalyticsEvents.ERROR_OCCURRED}: type=$errorType, message=$errorMessage")
    }
}

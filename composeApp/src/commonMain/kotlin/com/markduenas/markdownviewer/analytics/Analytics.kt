package com.markduenas.markdownviewer.analytics

/**
 * Analytics events for the Markdown Mermaid app
 */
object AnalyticsEvents {
    const val FILE_OPENED = "file_opened"
    const val URL_OPENED = "url_opened"
    const val MERMAID_FULLSCREEN = "mermaid_fullscreen"
    const val RECENT_FILE_OPENED = "recent_file_opened"
    const val ABOUT_SHOWN = "about_shown"
    const val MORE_APPS_CLICKED = "more_apps_clicked"
    const val FILE_CLOSED = "file_closed"
    const val ERROR_OCCURRED = "error_occurred"
}

object AnalyticsParams {
    const val FILE_NAME = "file_name"
    const val FILE_EXTENSION = "file_extension"
    const val SOURCE = "source"
    const val URL = "url"
    const val ERROR_MESSAGE = "error_message"
    const val ERROR_TYPE = "error_type"
}

expect object Analytics {
    fun logEvent(event: String, params: Map<String, String> = emptyMap())
    fun logFileOpened(fileName: String, source: String)
    fun logUrlOpened(url: String)
    fun logMermaidFullscreen()
    fun logRecentFileOpened(fileName: String)
    fun logAboutShown()
    fun logMoreAppsClicked()
    fun logFileClosed()
    fun logError(errorType: String, errorMessage: String)
}

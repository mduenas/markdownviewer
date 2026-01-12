package com.markduenas.markdownviewer.model

/**
 * Represents the current state of the markdown viewer.
 */
data class ViewerState(
    val source: ContentSource? = null,
    val content: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val zoomLevel: Float = 1.0f
) {
    /**
     * Returns the display title based on the current source.
     */
    val title: String
        get() = when (val src = source) {
            is ContentSource.LocalFile -> src.name
            is ContentSource.RemoteUrl -> src.displayName
            null -> "Markdown Viewer"
        }

    /**
     * Returns true if there is content to display.
     */
    val hasContent: Boolean
        get() = content.isNotEmpty() && source != null
}

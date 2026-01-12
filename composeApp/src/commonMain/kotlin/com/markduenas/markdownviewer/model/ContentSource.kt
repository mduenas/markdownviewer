package com.markduenas.markdownviewer.model

import kotlinx.serialization.Serializable

/**
 * Represents the source of markdown content being viewed.
 */
@Serializable
sealed class ContentSource {
    /**
     * Content loaded from a local file.
     */
    @Serializable
    data class LocalFile(
        val path: String,
        val name: String
    ) : ContentSource()

    /**
     * Content loaded from a remote URL.
     */
    @Serializable
    data class RemoteUrl(
        val url: String
    ) : ContentSource() {
        val displayName: String
            get() = url.substringAfterLast('/').takeIf { it.isNotEmpty() } ?: url
    }
}

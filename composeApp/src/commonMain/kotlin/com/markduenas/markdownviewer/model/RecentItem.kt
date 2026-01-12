package com.markduenas.markdownviewer.model

import kotlinx.serialization.Serializable

/**
 * Type of recent item.
 */
@Serializable
enum class RecentItemType {
    FILE,
    URL
}

/**
 * Represents a recently opened file or URL.
 */
@Serializable
data class RecentItem(
    val type: RecentItemType,
    val path: String,
    val displayName: String,
    val lastOpened: Long
) {
    companion object {
        fun fromSource(source: ContentSource): RecentItem {
            return when (source) {
                is ContentSource.LocalFile -> RecentItem(
                    type = RecentItemType.FILE,
                    path = source.path,
                    displayName = source.name,
                    lastOpened = currentTimeMillis()
                )
                is ContentSource.RemoteUrl -> RecentItem(
                    type = RecentItemType.URL,
                    path = source.url,
                    displayName = source.displayName,
                    lastOpened = currentTimeMillis()
                )
            }
        }
    }
}

/**
 * Returns current time in milliseconds.
 * Uses expect/actual for platform-specific implementation.
 */
expect fun currentTimeMillis(): Long

package com.markduenas.markdownviewer.data

import com.markduenas.markdownviewer.model.RecentItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Platform-specific key-value storage.
 */
expect class KeyValueStorage {
    fun getString(key: String, defaultValue: String): String
    fun putString(key: String, value: String)
}

/**
 * Repository for managing recent files and URLs.
 */
class RecentItemsRepository(
    private val storage: KeyValueStorage
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val maxItems = 10

    /**
     * Get list of recent items, sorted by most recent first.
     */
    fun getRecentItems(): List<RecentItem> {
        val jsonString = storage.getString(RECENT_ITEMS_KEY, "[]")
        return try {
            json.decodeFromString<List<RecentItem>>(jsonString)
                .sortedByDescending { it.lastOpened }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Add a new recent item. If it already exists, updates the timestamp.
     */
    fun addRecentItem(item: RecentItem) {
        val current = getRecentItems().toMutableList()

        // Remove existing item with same path (if any)
        current.removeAll { it.path == item.path }

        // Add new item at the beginning
        current.add(0, item)

        // Keep only the most recent items
        val trimmed = current.take(maxItems)

        // Save
        val jsonString = json.encodeToString(trimmed)
        storage.putString(RECENT_ITEMS_KEY, jsonString)
    }

    /**
     * Remove a specific item from recent list.
     */
    fun removeRecentItem(path: String) {
        val current = getRecentItems().toMutableList()
        current.removeAll { it.path == path }
        val jsonString = json.encodeToString(current)
        storage.putString(RECENT_ITEMS_KEY, jsonString)
    }

    /**
     * Clear all recent items.
     */
    fun clearRecentItems() {
        storage.putString(RECENT_ITEMS_KEY, "[]")
    }

    companion object {
        private const val RECENT_ITEMS_KEY = "recent_items"
    }
}

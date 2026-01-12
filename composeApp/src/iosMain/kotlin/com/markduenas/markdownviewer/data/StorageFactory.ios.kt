package com.markduenas.markdownviewer.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberKeyValueStorage(): KeyValueStorage {
    return remember { KeyValueStorage() }
}

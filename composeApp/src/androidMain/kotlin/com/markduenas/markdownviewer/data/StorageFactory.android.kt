package com.markduenas.markdownviewer.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberKeyValueStorage(): KeyValueStorage {
    val context = LocalContext.current
    return remember { KeyValueStorage(context) }
}

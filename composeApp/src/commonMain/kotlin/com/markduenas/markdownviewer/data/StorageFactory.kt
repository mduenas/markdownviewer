package com.markduenas.markdownviewer.data

import androidx.compose.runtime.Composable

/**
 * Creates a KeyValueStorage instance.
 * Platform-specific implementation handles context requirements.
 */
@Composable
expect fun rememberKeyValueStorage(): KeyValueStorage

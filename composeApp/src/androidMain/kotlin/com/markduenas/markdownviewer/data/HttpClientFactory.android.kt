package com.markduenas.markdownviewer.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

actual fun createHttpClient(): HttpClient = HttpClient(Android) {
    engine {
        connectTimeout = 30_000
        socketTimeout = 30_000
    }
}

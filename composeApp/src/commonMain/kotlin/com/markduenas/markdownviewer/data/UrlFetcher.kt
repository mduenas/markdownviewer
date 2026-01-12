package com.markduenas.markdownviewer.data

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class UrlFetcher(private val client: HttpClient = createHttpClient()) {

    suspend fun fetch(url: String): Result<String> = runCatching {
        require(url.startsWith("https://") || url.startsWith("http://")) {
            "URL must start with https:// or http://"
        }

        val response = client.get(url)

        val contentLength = response.headers["Content-Length"]?.toLongOrNull() ?: 0
        require(contentLength <= MAX_SIZE_BYTES) {
            "File too large (max ${MAX_SIZE_BYTES / 1024 / 1024}MB)"
        }

        response.bodyAsText()
    }

    companion object {
        private const val MAX_SIZE_BYTES = 5 * 1024 * 1024L // 5MB
    }
}

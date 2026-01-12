package com.markduenas.markdownviewer.data

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient

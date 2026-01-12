package com.markduenas.markdownviewer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getAppVersion(): String

data class InitialContent(
    val content: String,
    val fileName: String
)

expect fun getInitialContent(): InitialContent?

expect fun clearInitialContent()
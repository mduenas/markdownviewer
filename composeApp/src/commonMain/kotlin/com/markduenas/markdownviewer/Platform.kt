package com.markduenas.markdownviewer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
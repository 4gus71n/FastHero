package com.fast.hero

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
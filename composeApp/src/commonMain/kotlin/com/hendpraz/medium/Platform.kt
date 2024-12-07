package com.hendpraz.medium

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
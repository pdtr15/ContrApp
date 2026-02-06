package com.example.contraap

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
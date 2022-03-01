package com.niqr.weatherlisttest.requests

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val appClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            isLenient = true
        })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10000
    }
}
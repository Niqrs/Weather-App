package com.niqr.weatherlisttest.requests

import android.location.Location
import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

suspend fun HttpClient.currentWeatherRequest(location: Location): HttpCurrentWeather.Body {
    Log.d("", "${location}")
    val response: HttpResponse = this.get("https://api.openweathermap.org/data/2.5/weather") {
        parameter("lat", "${location.latitude}")
        parameter("lon", "${location.longitude}")
        parameter("units", "metric")
        parameter("appid", "e9213225af952422c82db3a81726e2a1")
    }.body()
    Log.d("", response.toString())
    return response.body()
}
suspend fun HttpClient.weatherForestRequest(location: Location): HttpForecast.Body =
    this.get("https://api.openweathermap.org/data/2.5/forecast") {
        parameter("lat", "${location.latitude}")
        parameter("lon", "${location.longitude}")
        parameter("units", "metric")
        parameter("appid", "e9213225af952422c82db3a81726e2a1")
    }.body()
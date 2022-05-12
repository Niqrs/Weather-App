package com.niqr.weatherlisttest.requests

import android.location.Location
import com.niqr.weatherapp.requests.getApiKey
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

suspend fun HttpClient.currentWeatherRequest(location: Location): HttpCurrentWeather.Body =
    this.get("https://api.openweathermap.org/data/2.5/weather") {
        parameter("lat", "${location.latitude}")
        parameter("lon", "${location.longitude}")
        parameter("units", "metric")
        parameter("appid", getApiKey())
    }.body()

suspend fun HttpClient.weatherForestRequest(location: Location): HttpForecast.Body =
    this.get("https://api.openweathermap.org/data/2.5/forecast") {
        parameter("lat", "${location.latitude}")
        parameter("lon", "${location.longitude}")
        parameter("units", "metric")
        parameter("appid", getApiKey())
    }.body()
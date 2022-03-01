package com.niqr.weatherlisttest.requests

import android.location.Location

suspend fun getCurrentWeather(location: Location): CurrentWeather =
    appClient.currentWeatherRequest(location).toCurrentWeather()


suspend fun getForecast(location: Location): List<WeatherForecast> =
    appClient.weatherForestRequest(location).toForecasts()
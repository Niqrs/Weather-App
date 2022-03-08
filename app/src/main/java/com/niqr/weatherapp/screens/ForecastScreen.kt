package com.niqr.weatherapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.niqr.weatherapp.feature.Lce
import com.niqr.weatherlisttest.requests.WeatherForecast

@Composable
fun ForecastScreen(state: Lce<List<WeatherForecast>>?) {
    when (state) {
        is Lce.Loading -> Loading()
        is Lce.Content -> Content(state.data)
        is Lce.Error -> Error(state.error)
    }
}


@Composable
private fun Loading() {
    Text("Loading...")
}

@Composable
private fun Content(forecasts: List<WeatherForecast>) {
    Forecasts(weatherForecasts = forecasts)
//    Text(currentWeather.icon)
//    Text(currentWeather.temperature.toString())
//    Text(currentWeather.mainWeather)
//    Text(currentWeather.humidity.toString())
//    Text(currentWeather.pressure.toString())
//    Text(currentWeather.windSpeed.toString())
}

@Composable
private fun Error(error: Throwable) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        when (error) {
            is IllegalArgumentException -> {
                Text("Swipe to Refresh")
            }
            else -> {
                Text("Something went wrong, may try later or swipe to Refresh")
            }
        }
    }
    error.printStackTrace()
}

@Composable
private fun Forecasts(weatherForecasts: List<WeatherForecast>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(weatherForecasts) { index, weatherForecast ->
            when (index) {
                0 -> Text(weatherForecast.dayOfWeek)
                else -> {
                    if (weatherForecast.dayOfWeek != weatherForecasts[index - 1].dayOfWeek)
                        Text(weatherForecast.dayOfWeek)
                }
            }
            Text("${weatherForecast.time} | ${weatherForecast.weather} | ${weatherForecast.temperature}")
        }
    }
}
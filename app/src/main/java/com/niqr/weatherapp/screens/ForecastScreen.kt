package com.niqr.weatherapp.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
    when (error) {
        is IllegalArgumentException -> {
            Text("Something with request, may try later")
        }
        else -> {
            Text("Something went wrong, may try later")
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
            Text("${ if (weatherForecast.time.hour < 10) "0" else ""}${weatherForecast.time.hour}:00 | ${weatherForecast.weather} | ${weatherForecast.temperature}")
        }
    }
}
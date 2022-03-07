package com.niqr.weatherapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.niqr.weatherapp.feature.Lce
import com.niqr.weatherlisttest.requests.CurrentWeather

@Composable
fun TodayScreen(state: Lce<CurrentWeather>?) {
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
private fun Content(currentWeather: CurrentWeather) {
    TodayWeather(currentWeather = currentWeather)
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
                Text("Something with request, may try later or swipe to Refresh")
            }
            else -> {
                Text("Something went wrong, may try later or swipe to Refresh")
            }
        }
    }
    error.printStackTrace()
}

@Composable
fun TodayWeather(currentWeather: CurrentWeather) {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Text("icon: ${currentWeather.icon}")
        Text("temperature: ${currentWeather.temperature}")
        Text("mainWeather: ${currentWeather.mainWeather}")

        Text("humidity: ${currentWeather.humidity}")
        Text("pressure: ${currentWeather.pressure}")
        Text("windSpeed: ${currentWeather.windSpeed}")
    }
}
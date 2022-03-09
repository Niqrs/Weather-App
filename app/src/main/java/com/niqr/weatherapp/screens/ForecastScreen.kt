package com.niqr.weatherapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niqr.weatherapp.feature.Lce
import com.niqr.weatherapp.feature.getWeatherIconId
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

@Preview
@Composable
private fun Forecasts(
    @PreviewParameter(ForecastsWeatherPreviewParameterProvider::class)
    weatherForecasts: List<WeatherForecast>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { Spacer(modifier = Modifier.height(6.dp)) }
        itemsIndexed(weatherForecasts) { index, weatherForecast ->
            when (index) {
                0 -> {
                    Text(weatherForecast.dayOfWeek)
                    Spacer(modifier = Modifier.height(6.dp))
                }
                else -> {
                    if (weatherForecast.dayOfWeek != weatherForecasts[index - 1].dayOfWeek) {
                        Text(weatherForecast.dayOfWeek)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
            ForecastCard(weatherForecast)
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
fun ForecastCard(weatherForecast: WeatherForecast) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = getWeatherIconId(weatherForecast.icon)),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = weatherForecast.time)
            Text(text = weatherForecast.weather)
        }
        Text(
            text = "${weatherForecast.temperature}°",
            fontSize = 64.sp
        )
    }
}

private class ForecastsWeatherPreviewParameterProvider :
    PreviewParameterProvider<List<WeatherForecast>> {
    override val values = sequenceOf(
        listOf(
            WeatherForecast(icon = "01d", time = "00:00", weather = "Clear", temperature = 20, dayOfWeek = "MONDAY"),
            WeatherForecast(icon = "01d", time = "03:00", weather = "Clear", temperature = 20, dayOfWeek = "MONDAY"),
            WeatherForecast(icon = "01d", time = "06:00", weather = "Clear", temperature = 20, dayOfWeek = "MONDAY"),
            WeatherForecast(icon = "01d", time = "09:00", weather = "Clear", temperature = 20, dayOfWeek = "MONDAY"),
            WeatherForecast(icon = "01d", time = "12:00", weather = "Clear", temperature = 20, dayOfWeek = "MONDAY"),
            WeatherForecast(icon = "01d", time = "15:00", weather = "Clear", temperature = 20, dayOfWeek = "MONDAY"),
            WeatherForecast(icon = "01d", time = "18:00", weather = "Clear", temperature = 20, dayOfWeek = "MONDAY"),
            WeatherForecast(icon = "01d", time = "21:00", weather = "Clear", temperature = 20, dayOfWeek = "MONDAY"),
            WeatherForecast(icon = "01d", time = "00:00", weather = "Clear", temperature = 20, dayOfWeek = "TUESDAY"),
            WeatherForecast(icon = "01d", time = "03:00", weather = "Clear", temperature = 20, dayOfWeek = "TUESDAY"),
            WeatherForecast(icon = "01d", time = "06:00", weather = "Clear", temperature = 20, dayOfWeek = "TUESDAY"),
            WeatherForecast(icon = "01d", time = "09:00", weather = "Clear", temperature = 20, dayOfWeek = "TUESDAY"),
            WeatherForecast(icon = "01d", time = "12:00", weather = "Clear", temperature = 20, dayOfWeek = "TUESDAY"),
            WeatherForecast(icon = "01d", time = "15:00", weather = "Clear", temperature = 20, dayOfWeek = "TUESDAY"),
            WeatherForecast(icon = "01d", time = "18:00", weather = "Clear", temperature = 20, dayOfWeek = "TUESDAY"),
            WeatherForecast(icon = "01d", time = "21:00", weather = "Clear", temperature = 20, dayOfWeek = "TUESDAY"),
            WeatherForecast(icon = "01d", time = "00:00", weather = "Clear", temperature = 20, dayOfWeek = "WEDNESDAY"),
            WeatherForecast(icon = "01d", time = "03:00", weather = "Clear", temperature = 20, dayOfWeek = "WEDNESDAY"),
            WeatherForecast(icon = "01d", time = "06:00", weather = "Clear", temperature = 20, dayOfWeek = "WEDNESDAY"),
            WeatherForecast(icon = "01d", time = "09:00", weather = "Clear", temperature = 20, dayOfWeek = "WEDNESDAY"),
            WeatherForecast(icon = "01d", time = "12:00", weather = "Clear", temperature = 20, dayOfWeek = "WEDNESDAY"),
            WeatherForecast(icon = "01d", time = "15:00", weather = "Clear", temperature = 20, dayOfWeek = "WEDNESDAY"),
            WeatherForecast(icon = "01d", time = "18:00", weather = "Clear", temperature = 20, dayOfWeek = "WEDNESDAY"),
            WeatherForecast(icon = "01d", time = "21:00", weather = "Clear", temperature = 20, dayOfWeek = "WEDNESDAY"),

        )
    )
}
package com.niqr.weatherapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niqr.weatherapp.R
import com.niqr.weatherapp.feature.Lce
import com.niqr.weatherapp.feature.getWeatherIconId
import com.niqr.weatherapp.ui.animations.DotsPulsing
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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DotsPulsing()
    }
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
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(256.dp).aspectRatio(1f),
            painter = painterResource(R.drawable.ic_sad_cloud_24dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text("Something went wrong", color = MaterialTheme.colorScheme.primary)
        Text("Swipe to Refresh", color = MaterialTheme.colorScheme.primary)
    }
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
                    Text(modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp), text = weatherForecast.dayOfWeek, color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Medium, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                else -> {
                    if (weatherForecast.dayOfWeek != weatherForecasts[index - 1].dayOfWeek) {
                        Text(modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp), text = weatherForecast.dayOfWeek, color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Medium, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            ForecastBlock(weatherForecast)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ForecastBlock(weatherForecast: WeatherForecast) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 8.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = getWeatherIconId(weatherForecast.icon)),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(with(LocalDensity.current) {
                        72.sp.toDp()
                    }),
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = weatherForecast.time, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = MaterialTheme.colorScheme.tertiary)
                Text(text = weatherForecast.weather, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = MaterialTheme.colorScheme.tertiary)
            }
            Text(
                modifier = Modifier.padding(horizontal = 6.dp),
                text = "${weatherForecast.temperature}°",
                fontSize = 64.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
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
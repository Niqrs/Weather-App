package com.niqr.weatherapp.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowRightAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.niqr.weatherlisttest.requests.CurrentWeather
import kotlin.math.roundToInt

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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DotsPulsing()
    }
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
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(256.dp)
                .aspectRatio(1f),
            painter = painterResource(R.drawable.ic_sad_cloud_24dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.run { if (isSystemInDarkTheme()) onTertiaryContainer else tertiary }
        )
        Text("Something went wrong", color = MaterialTheme.colorScheme.tertiary)
        Text("Swipe to Refresh", color = MaterialTheme.colorScheme.tertiary)
    }
}

@Preview("TodayScreen")
@Composable
private fun TodayWeather(
    @PreviewParameter(CurrentWeatherPreviewParameterProvider::class)
    currentWeather: CurrentWeather
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MainInformation(currentWeather, modifier = Modifier.padding(vertical = 8.dp))
        Divider(modifier = Modifier.fillMaxWidth().height(2.dp), color = MaterialTheme.colorScheme.surfaceVariant)
        AdditionalInformation(currentWeather, modifier = Modifier.padding(vertical = 8.dp))
        Divider(modifier = Modifier.fillMaxWidth(0.6f).height(2.dp), color = MaterialTheme.colorScheme.surfaceVariant)
        SunMovementInformation(currentWeather, modifier = Modifier.padding(vertical = 8.dp))
        Divider(modifier = Modifier.fillMaxWidth().height(2.dp), color = MaterialTheme.colorScheme.surfaceVariant)
    }
}

@Composable
private fun MainInformation(currentWeather: CurrentWeather, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 8.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(getWeatherIconId(currentWeather.icon)),
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.run { if (isSystemInDarkTheme()) onTertiaryContainer else tertiary }
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${currentWeather.mainWeather} | ${currentWeather.temperature}°",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.run { if (isSystemInDarkTheme()) onTertiaryContainer else tertiary },
                )
                Text(
                    "Feels like: ${currentWeather.feelsLike}°C",
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}

@Composable
private fun AdditionalInformation(currentWeather: CurrentWeather, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        WeatherInfoBlock(modifier = Modifier
            .weight(1f), icon = R.drawable.ic_wind_speed_24dp,info = "${currentWeather.windSpeed.roundToInt()} km/h")
        Spacer(modifier = Modifier.width(24.dp))
        WeatherInfoBlock(modifier = Modifier
            .weight(1f), icon = R.drawable.ic_humidity_24dp,info = "${currentWeather.humidity}%")
        Spacer(modifier = Modifier.width(24.dp))
        WeatherInfoBlock(modifier = Modifier
            .weight(1f), icon = R.drawable.ic_atmospheric_pressure_24dp,info = "${currentWeather.pressure} hPa")
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun SunMovementInformation(currentWeather: CurrentWeather, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        WeatherInfoBlock(modifier = Modifier
            .weight(1f),icon = R.drawable.ic_weather_sunrice_24dp,info = currentWeather.sunriseTime)
        Icon(
            imageVector = Icons.Rounded.ArrowRightAlt,
            contentDescription = null,
            Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .weight(1f),
            tint = MaterialTheme.colorScheme.run { if (isSystemInDarkTheme()) onTertiaryContainer else tertiary }
        )
        WeatherInfoBlock(modifier = Modifier
            .weight(1f),icon = R.drawable.ic_weather_sunset_24dp,info = currentWeather.sunsetTime)
        Spacer(modifier = Modifier.width(16.dp))

    }
}

@Composable
private fun WeatherInfoBlock(modifier: Modifier = Modifier, icon: Int, info: String? = null) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            tonalElevation = 8.dp,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    modifier = Modifier.aspectRatio(1f),
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.run { if (isSystemInDarkTheme()) onTertiaryContainer else tertiary }
                )
                if (info != null) {
                    Text(
                        text = info,
                        color = MaterialTheme.colorScheme.tertiary,
                        maxLines = 1
                    )
                }
            }
        }
}

private class CurrentWeatherPreviewParameterProvider : PreviewParameterProvider<CurrentWeather> {
    override val values = sequenceOf(
        CurrentWeather(
            icon ="04n",
            temperature = -6,
            feelsLike = -8,
            mainWeather = "Clouds",
            cityName = "Moscow",
            country = "RU",

            humidity = 63,
            pressure = 1020,
            windSpeed = 6.0f,

            sunriseTime = "07:26",
            sunsetTime = "22:26"
        ),
    )
}

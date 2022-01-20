package com.niqr.weatherapp.navigation

import android.graphics.drawable.Icon
import androidx.compose.material3.Icon
import com.niqr.weatherapp.R


sealed class Screen(val route: String, val icon: Int? = null) {
    object Today : Screen("today", R.drawable.ic_weather_day_clear_sky_24dp)
    object  Forecast : Screen("forecast", R.drawable.ic_weather_night_rain_24dp)
}

val bottomBarScreens = listOf(
    Screen.Today,
    Screen.Forecast
)

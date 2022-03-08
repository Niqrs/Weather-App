package com.niqr.weatherapp.feature

import com.niqr.weatherapp.R

fun getWeatherIconId(icon: String): Int = when(icon) {
    "01d" -> R.drawable.ic_weather_day_clear_sky_24dp
    "01n" -> R.drawable.ic_weather_night_clear_sky_24dp

    "02d" -> R.drawable.ic_weather_day_few_clouds_24dp
    "02n" -> R.drawable.ic_weather_night_few_clouds_24dp

    "03d" -> R.drawable.ic_weather_day_scattered_clouds_24dp
    "03n" -> R.drawable.ic_weather_night_scattered_clouds_24dp

    "04d" -> R.drawable.ic_weather_day_broken_clouds_24dp
    "04n" -> R.drawable.ic_weather_night_broken_clouds_24dp

    "09d" -> R.drawable.ic_weather_day_shower_rain_24dp
    "09n" -> R.drawable.ic_weather_night_shower_rain_24dp

    "10d" -> R.drawable.ic_weather_day_rain_24dp
    "10n" -> R.drawable.ic_weather_night_rain_24dp

    "11d" -> R.drawable.ic_weather_day_thunderstorm_24dp
    "11n" -> R.drawable.ic_weather_night_thunderstorm_24dp

    "13d" -> R.drawable.ic_weather_day_snow_24dp
    "13n" -> R.drawable.ic_weather_night_snow_24dp

    "50d" -> R.drawable.ic_weather_day_mist_24dp
    "50n" -> R.drawable.ic_weather_night_mist_24dp
    else -> R.drawable.ic_weather_day_clear_sky_24dp
}
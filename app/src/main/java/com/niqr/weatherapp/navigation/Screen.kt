package com.niqr.weatherapp.navigation

sealed class Screen(val route: String) {
    object Today : Screen("today")
    object  Forecast : Screen("forecast")
}

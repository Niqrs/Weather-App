package com.niqr.weatherapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.niqr.weatherapp.navigation.Screen
import com.niqr.weatherapp.navigation.screens.ForecastScreen
import com.niqr.weatherapp.navigation.screens.TodayScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "today") {
        composable(Screen.Today.route) { TodayScreen() }
        composable(Screen.Forecast.route) { ForecastScreen() }
    }
}
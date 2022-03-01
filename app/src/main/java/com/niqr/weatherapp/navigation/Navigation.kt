package com.niqr.weatherapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.niqr.weatherapp.navigation.Screen
import com.niqr.weatherapp.navigation.bottomBarScreens
import com.niqr.weatherapp.screens.ForecastScreen
import com.niqr.weatherapp.screens.TodayScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {

        NavHost(navController = navController, startDestination = "today") {

            composable(Screen.Today.route) { TodayScreen() }
            composable(Screen.Forecast.route) { ForecastScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar() {

        bottomBarScreens.forEach { screen ->

            NavigationBarItem(
                icon = { Icon(painter = painterResource(screen.icon!!), contentDescription = null) },
                label = { Text(screen.route.replaceFirstChar{ it.uppercaseChar() }) },
                selected = currentRoute == screen.route,
                onClick = {

                    navController.navigate(screen.route) {

                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
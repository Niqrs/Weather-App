package com.niqr.weatherapp

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.niqr.weatherapp.feature.Lce
import com.niqr.weatherapp.navigation.Screen
import com.niqr.weatherapp.navigation.bottomBarScreens
import com.niqr.weatherapp.screens.ForecastScreen
import com.niqr.weatherapp.screens.TodayScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(viewModel: WeatherViewModel, activity: MainActivity) {

    val navController = rememberNavController()


    when(viewModel.locationPermissionState) {
        LocationPermissionState.PERMISSION_GRANTED -> {
            viewModel.isLocationEnabled = activity.isLocationEnabled()
            Button(onClick = {}) {
                Text("CurrentWeather")
            }

            SwipeRefresh(
                modifier = Modifier.fillMaxSize(),
                state = rememberSwipeRefreshState(viewModel.isRefreshing),
                onRefresh = {
                    viewModel.isRefreshing = true
                    viewModel.updateWeather(activity::getCurrentLocation)
                },
            ) {
                if (viewModel.isInternetAvailable || viewModel.forecastsState is Lce.Content) {
                    if (viewModel.isLocationEnabled!! && viewModel.forecastsState == null) {
                        viewModel.updateWeather(activity::getCurrentLocation)
                    } else if (!viewModel.isLocationEnabled!!) {
                        activity.createLocationRequest()
                    }

                    Scaffold(
                        bottomBar = { BottomNavigationBar(navController) }
                    ) {


                        NavHost(navController = navController, startDestination = "today") {


                            composable(Screen.Today.route) { TodayScreen(viewModel.currentWeatherState) }
                            composable(Screen.Forecast.route) { ForecastScreen(viewModel.forecastsState) }
                        }
                    }
                } else {
                    ClickableText(text = AnnotatedString("Turn on Internet please"), onClick = {})
                }
            }
        }

        LocationPermissionState.PERMISSION_DENIED -> {
            Button(onClick = { activity.settingsActivityIntent(activity) }) {
                Text("Open Settings")
            }
        }

        LocationPermissionState.SHOULD_SHOW_REQUEST_PERMISSION_RATIONALE -> {
            //TODO: showInContextUI(...)
            Button(onClick = activity::locationPermissionRequest
            ) {
                Text("WAITING RESPONSE")
            }
        }

        LocationPermissionState.WAITING_RESPONSE -> {
            Text("WAITING RESPONSE2")
        }
    }
    if (viewModel.locationPermissionState != null) {
        Log.d("", viewModel.locationPermissionState!!.message)
    } else {
        Log.d("", "NUUUL")
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
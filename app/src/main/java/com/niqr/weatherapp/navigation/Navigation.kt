package com.niqr.weatherapp.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.niqr.weatherapp.LocationPermissionState
import com.niqr.weatherapp.MainActivity
import com.niqr.weatherapp.WeatherViewModel
import com.niqr.weatherapp.feature.Lce
import com.niqr.weatherapp.screens.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(viewModel: WeatherViewModel, activity: MainActivity) {

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    when(viewModel.locationPermissionState) {
        LocationPermissionState.PERMISSION_GRANTED -> {
            viewModel.isLocationEnabled = activity.isLocationEnabled()
    
            SwipeRefresh(
                modifier = Modifier.fillMaxSize(),
                state = rememberSwipeRefreshState(viewModel.isRefreshing),
                onRefresh = {
                    viewModel.isRefreshing = true
                    viewModel.isLocationEnabled = activity.isLocationEnabled()
                    if (viewModel.isInternetAvailable) {
                        if (viewModel.isLocationEnabled!!) {
                            viewModel.updateWeather(activity::getLocation)
                        } else {
                            activity.createLocationRequest()
                            viewModel.isRefreshing = false
                        }
                    } else {
                        if (snackbarHostState.currentSnackbarData == null)
                            scope.launch {
                                snackbarHostState.showSnackbar("Can't update weather without internet")
                            }
                        viewModel.isRefreshing = false
                    }
                },
            ) {
                if (viewModel.isInternetAvailable || viewModel.forecastsState is Lce.Content) {
                    if (viewModel.isLocationEnabled!! && viewModel.forecastsState == null) {
                        viewModel.updateWeather(activity::getLocation)
                    } else if (!viewModel.isLocationEnabled!!) {
                        activity.createLocationRequest()
                    }

                    Scaffold(
                        topBar = { TopAppBar(navController) { viewModel.dynamicColors = !viewModel.dynamicColors } },
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                        bottomBar = { BottomNavigationBar(navController) },

                    ) { innerPadding ->
                        NavHost(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(innerPadding),
                            navController = navController,
                            startDestination = "today"
                        ) {
                            composable(Screen.Today.route) { TodayScreen(viewModel.currentWeatherState) }
                            composable(Screen.Forecast.route) { ForecastScreen(viewModel.forecastsState) }
                        }
                    }
                } else {
                    Scaffold(topBar = {CenterAlignedTopAppBar(title = { Text(text = "Error") })}) { innerPadding ->
                        Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                            Text(text = AnnotatedString("Turn on the internet please"), color = MaterialTheme.colorScheme.tertiary)
                        }
                    }
                }
            }
        }

        LocationPermissionState.PERMISSION_DENIED ->
            LocationPermissionDeniedScreen(onClick = activity::settingsActivityIntent)

        LocationPermissionState.SHOULD_SHOW_REQUEST_PERMISSION_RATIONALE ->
            ShouldShowRequestPermissionRationaleScreen(onClick = activity::locationPermissionRequest)

        LocationPermissionState.WAITING_RESPONSE ->
            WaitingLocationResponseScreen()
    }
}

@Composable
fun TopAppBar(navController: NavController, onActionClick: () -> Unit) {
    Column {
        CenterAlignedTopAppBar(
//        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.run { if (isSystemInDarkTheme()) secondaryContainer else primary }),
            title = { navController.currentBackStackEntryAsState().value?.destination?.route?.let { route ->
                Text(route.replaceFirstChar{ it.uppercaseChar() }, color = MaterialTheme.colorScheme.run { if (isSystemInDarkTheme()) onTertiaryContainer else tertiary })
            } },
            actions = {
/*                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    IconButton(onClick = onActionClick) {
                        Icon(imageVector = Icons.Rounded.ColorLens, contentDescription = null, tint =  MaterialTheme.colorScheme.run { if (isSystemInDarkTheme()) onTertiaryContainer else tertiary })
                    }
                }*/
            }
        )
        Divider(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp), color = MaterialTheme.colorScheme.surfaceVariant)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {

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
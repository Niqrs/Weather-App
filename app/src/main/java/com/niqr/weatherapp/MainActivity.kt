package com.niqr.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.location.LocationManagerCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.niqr.weatherapp.navigation.Navigation
import com.niqr.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.tasks.await


class MainActivity : ComponentActivity() {

    private var isResolutionLaunched: Boolean = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var resolutionForResult: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var locationAccessibilityLauncher: ActivityResultLauncher<Intent>
    private var locationCancellationTokenSource = CancellationTokenSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isResolutionLaunched = false

        val viewModel: WeatherViewModel by viewModels()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            interval = 10000
            fastestInterval = 5000
            maxWaitTime = 2000
        }

        requestLocationPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    viewModel.locationPermissionState = LocationPermissionState.PERMISSION_GRANTED
                } else {
                    viewModel.locationPermissionState =
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                            LocationPermissionState.SHOULD_SHOW_REQUEST_PERMISSION_RATIONALE
                        else
                            LocationPermissionState.PERMISSION_DENIED
                }
            }

        resolutionForResult =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
                if (activityResult.resultCode == RESULT_OK) {
                    isResolutionLaunched = false
                    viewModel.isLocationEnabled = true
                } else {
                    isResolutionLaunched = false
                    viewModel.isLocationEnabled = false
                    viewModel.isRefreshing = false
                    locationCancellationTokenSource.cancel()
                    locationCancellationTokenSource = CancellationTokenSource()
                    viewModel.setErrorState()
                }
            }

        locationAccessibilityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.updateLocationPermissionState(this)
        }

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.i("Test", "Default -> Network Available")
                viewModel.isInternetAvailable = true
            }

            override fun onLost(network: Network) {
                Log.i("Test", "Default -> Connection lost")
                viewModel.isInternetAvailable = false
                viewModel.isRefreshing = false
            }
        })

        setContent {
            WeatherAppTheme(dynamicColor = viewModel.dynamicColors) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    tonalElevation = 1.dp
                ) {
                    App(viewModel = viewModel, activity = this)
                }
            }
        }
    }

    fun settingsActivityIntent() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        locationAccessibilityLauncher.launch(intent)
    }

    fun locationPermissionRequest() =
        requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

    fun createLocationRequest() {
        val builder = LocationSettingsRequest.Builder()
            .setAlwaysShow(true)
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { //Todo: it is work fine
            return@addOnSuccessListener
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    if (!isResolutionLaunched)
                        startLocationResolution(exception.resolution)
                } catch (sendEx: IntentSender.SendIntentException) {
                // Ignore the error.
                }
            }
        }
    }

    private fun startLocationResolution(resolution: PendingIntent) {
        isResolutionLaunched = true
        val intentSenderRequest = IntentSenderRequest.Builder(resolution).build()
        resolutionForResult.launch(intentSenderRequest)
    }

    fun isLocationEnabled() = LocationManagerCompat.isLocationEnabled(this.getSystemService(
        LocationManager::class.java))

    suspend fun getLocation(): Location {
        var location: Location?
        location = getLastKnownLocation().await()
        if (location == null)
            location = getCurrentLocation().await()
        return location
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(): Task<Location> = fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, locationCancellationTokenSource.token)

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Task<Location?> = fusedLocationClient.lastLocation
}

@Composable
fun App(viewModel: WeatherViewModel, activity: MainActivity) {// Remember a SystemUiController
    val systemUiController = rememberSystemUiController()
//    val useDarkIcons = isSystemInDarkTheme()
    val statusBarColor = MaterialTheme.colorScheme.run { if (isSystemInDarkTheme()) secondaryContainer else tertiary }

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = statusBarColor,
//            darkIcons = useDarkIcons
        )
    }

    viewModel.updateLocationPermissionState(activity)
    Navigation(viewModel, activity)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppTheme {
//        App()
    }
}
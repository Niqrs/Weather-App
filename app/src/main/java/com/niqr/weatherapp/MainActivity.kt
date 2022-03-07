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
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.niqr.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {

    private var isResolutionLaunched: Boolean = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var resolutionForResult: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isResolutionLaunched = false

        val viewModel: WeatherViewModel by viewModels()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
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
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION))
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
                }
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
            }
        })

        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(viewModel = viewModel, activity = this)
                }
            }
        }
    }

    fun settingsActivityIntent(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", ContactsContract.Directory.PACKAGE_NAME, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun locationPermissionRequest() =
        requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

    fun createLocationRequest() {
        val builder = LocationSettingsRequest.Builder()
            .setAlwaysShow(true)
            .setNeedBle(true)
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

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(): Task<Location> = fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
}

@Composable
fun App(viewModel: WeatherViewModel, activity: MainActivity) {
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
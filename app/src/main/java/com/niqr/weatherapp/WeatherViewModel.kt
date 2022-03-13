package com.niqr.weatherapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niqr.weatherapp.feature.Lce
import com.niqr.weatherlisttest.requests.CurrentWeather
import com.niqr.weatherlisttest.requests.WeatherForecast
import com.niqr.weatherlisttest.requests.getCurrentWeather
import com.niqr.weatherlisttest.requests.getForecast
import kotlinx.coroutines.launch
import java.nio.channels.UnresolvedAddressException


class WeatherViewModel: ViewModel() {

    var isRefreshing by mutableStateOf(false)
    var locationPermissionState by mutableStateOf<LocationPermissionState?>(null)
    var currentWeatherState by mutableStateOf<Lce<CurrentWeather>?>(null)
        private set
    var forecastsState by mutableStateOf<Lce<List<WeatherForecast>>?>(null)
        private set
    var isLocationEnabled by mutableStateOf<Boolean?>(null)
    var isInternetAvailable by mutableStateOf(false)

    fun updateLocationPermissionState(activity: MainActivity) =
        when {
            locationPermissionState == LocationPermissionState.SHOULD_SHOW_REQUEST_PERMISSION_RATIONALE ||
                    locationPermissionState == LocationPermissionState.WAITING_RESPONSE -> { }

            isLocationPermissionGranted(activity) -> {
                locationPermissionState = LocationPermissionState.PERMISSION_GRANTED
            }

            shouldShowLocationRequestPermissionRationale(activity) -> {
                locationPermissionState = LocationPermissionState.SHOULD_SHOW_REQUEST_PERMISSION_RATIONALE
            }

            locationPermissionState == LocationPermissionState.PERMISSION_DENIED -> {}
            else -> {
                locationPermissionState = LocationPermissionState.WAITING_RESPONSE
                activity.locationPermissionRequest()
            }
        }

    fun updateWeather(locationTask: suspend () -> Location?) = viewModelScope.launch {
        currentWeatherState = Lce.Loading
        forecastsState = Lce.Loading

        val location: Location? =
            try {
                if (isInternetAvailable) {
                    Log.d("", "FEfefefefefeffe1")
                    locationTask()
                }
                else
                {
                    Log.d("", "FEfefefefefeffe")
                    null
                }
            } catch (e: Exception) {
                null
            }

        updateCurrentWeather(location)
        updateForecast(location)
    }

    private suspend fun updateCurrentWeather(location: Location?)  {
        currentWeatherState =
            if (location == null)
                Lce.Error(Throwable())
            else
                try {
                    Lce.Content(getCurrentWeather(location))
                } catch (e: UnresolvedAddressException) {
                    Lce.Error(e)
                } catch (e: Exception) { //TODO: Remove
                    Lce.Error(e)
                }
        isRefreshing = false
    }

    private suspend fun updateForecast(location: Location?) {
        Log.d("", location.toString())
        Log.d("", "11111111111111111111111111111111111111")
        forecastsState =
            if (location == null)
                Lce.Error(Throwable())
            else
                try {
                    Lce.Content(getForecast(location))
                } catch (e: UnresolvedAddressException) {
                    Lce.Error(e)
                } catch (e: Exception) { //TODO: Remove
                    Lce.Error(e)
                }
        isRefreshing = false
    }

    fun setErrorState() {
        val state = Lce.Error(Throwable())
        currentWeatherState = state
        forecastsState = state
    }

    private fun isLocationPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowLocationRequestPermissionRationale(activity: Activity): Boolean =
        shouldShowRequestPermissionRationale(activity, ACCESS_COARSE_LOCATION)
}

enum class LocationPermissionState(val message: String) {
    PERMISSION_GRANTED("PERMISSION GRANTED"),
    PERMISSION_DENIED("PERMISSION DENIED"),
    SHOULD_SHOW_REQUEST_PERMISSION_RATIONALE("SHOULD SHOW REQUEST PERMISSION RATIONALE"),
    WAITING_RESPONSE("WAITING RESPONSE")
}
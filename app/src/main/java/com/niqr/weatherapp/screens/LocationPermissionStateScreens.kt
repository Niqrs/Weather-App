package com.niqr.weatherapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.niqr.weatherapp.R
import com.niqr.weatherapp.ui.animations.DotsPulsing

@Preview("LocationPermissionDeniedScreen")
@Composable
fun LocationPermissionDeniedScreen(
    @PreviewParameter(EmptyUnitPreviewParameterProvider::class)
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(196.dp).aspectRatio(1f),
            painter = painterResource(R.drawable.ic_location_settings_24dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Divider(modifier = Modifier.padding(bottom = 8.dp).width(96.dp))
        Text("WeatherApp needs your location permission", color = MaterialTheme.colorScheme.tertiary)
        Text("Click to open settings", color = MaterialTheme.colorScheme.tertiary)
    }
}

@Preview("ShouldShowRequestPermissionRationaleScreen")
@Composable
fun ShouldShowRequestPermissionRationaleScreen(
    @PreviewParameter(EmptyUnitPreviewParameterProvider::class)
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(196.dp).aspectRatio(1f),
            painter = painterResource(R.drawable.ic_location_settings_24dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Divider(modifier = Modifier.padding(bottom = 8.dp).width(96.dp))
        Text("WeatherApp needs your location permission", color = MaterialTheme.colorScheme.tertiary)
        Text("Click to open dialog", color = MaterialTheme.colorScheme.tertiary)
    }
}

@Preview("WaitingLocationResponseScreen")
@Composable
fun WaitingLocationResponseScreen() {
    DotsPulsing()
}

private class EmptyUnitPreviewParameterProvider : PreviewParameterProvider<() -> Unit> {
    override val values = sequenceOf(
        {}
    )
}
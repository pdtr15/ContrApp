package com.example.contraap.location

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined

@Composable
actual fun RequestLocationPermission(onResult: (Boolean) -> Unit) {
    LaunchedEffect(Unit) {
        val status = CLLocationManager.authorizationStatus()
        when (status) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways -> {
                onResult(true)
            }
            kCLAuthorizationStatusNotDetermined -> {
                // En iOS el permiso se pide desde el CLLocationManager delegate.
                // Por ahora notificamos false; el flujo real requiere un delegate nativo.
                onResult(false)
            }
            else -> onResult(false)
        }
    }
}
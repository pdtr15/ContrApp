package com.example.contraap.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale

actual class LocationManager(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    actual fun getCurrentLocation(
        onSuccess: (LocationData) -> Unit,
        onError: (String) -> Unit
    ) {
        // Verificar permisos
        if (!hasLocationPermission()) {
            onError("Se requieren permisos de ubicación")
            return
        }

        try {
            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Obtener dirección a partir de coordenadas
                    val address = getAddressFromLocation(location.latitude, location.longitude)

                    onSuccess(
                        LocationData(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            address = address
                        )
                    )
                } else {
                    onError("No se pudo obtener la ubicación")
                }
            }.addOnFailureListener { exception ->
                onError("Error: ${exception.message}")
            }
        } catch (e: SecurityException) {
            onError("Permisos de ubicación no concedidos")
        }
    }

    actual fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                buildString {
                    address.locality?.let { append(it) }
                    if (address.locality != null && address.adminArea != null) append(", ")
                    address.adminArea?.let { append(it) }
                    if (address.countryName != null) {
                        if (address.locality != null || address.adminArea != null) append(", ")
                        append(address.countryName)
                    }
                }
            } else {
                "$latitude, $longitude"
            }
        } catch (e: Exception) {
            "$latitude, $longitude"
        }
    }
}
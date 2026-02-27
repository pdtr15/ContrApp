package com.example.contraap.location

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.example.contraap.appContext

actual class LocationManager {

    actual fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    actual suspend fun getCurrentLocation(): Result<LocationData> =
        suspendCoroutine { continuation ->
            if (!hasLocationPermission()) {
                continuation.resume(Result.failure(Exception("Sin permiso de ubicación")))
                return@suspendCoroutine
            }

            val client = LocationServices.getFusedLocationProviderClient(appContext)
            val cancellationToken = CancellationTokenSource()

            client.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationToken.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    val address = try {
                        val geocoder = Geocoder(appContext, Locale.getDefault())
                        val results = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )
                        if (!results.isNullOrEmpty()) {
                            val addr = results[0]
                            "${addr.subLocality ?: addr.locality ?: ""}, ${addr.adminArea ?: ""}"
                        } else {
                            "${location.latitude}, ${location.longitude}"
                        }
                    } catch (e: Exception) {
                        "${location.latitude}, ${location.longitude}"
                    }

                    continuation.resume(
                        Result.success(
                            LocationData(
                                latitude  = location.latitude,
                                longitude = location.longitude,
                                address   = address
                            )
                        )
                    )
                } else {
                    continuation.resume(Result.failure(Exception("No se pudo obtener la ubicación")))
                }
            }.addOnFailureListener { e ->
                continuation.resume(Result.failure(e))
            }
        }
}
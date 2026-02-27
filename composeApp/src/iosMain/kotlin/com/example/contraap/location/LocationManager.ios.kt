@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.example.contraap.location

import platform.CoreLocation.*
import platform.Foundation.*
import platform.darwin.NSObject
import kotlinx.cinterop.useContents
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class LocationManager {

    private val manager = CLLocationManager()

    actual fun hasLocationPermission(): Boolean {
        val status = CLLocationManager.authorizationStatus()
        return status == kCLAuthorizationStatusAuthorizedWhenInUse ||
                status == kCLAuthorizationStatusAuthorizedAlways
    }

    actual suspend fun getCurrentLocation(): Result<LocationData> =
        suspendCoroutine { continuation ->
            if (!hasLocationPermission()) {
                continuation.resume(Result.failure(Exception("Sin permiso de ubicación")))
                return@suspendCoroutine
            }

            val delegate = IosLocationDelegate(continuation)
            manager.delegate = delegate
            manager.startUpdatingLocation()
        }

    // Inner class evita redeclaración entre iosArm64Main e iosSimulatorArm64Main
    private inner class IosLocationDelegate(
        private val continuation: Continuation<Result<LocationData>>
    ) : NSObject(), CLLocationManagerDelegateProtocol {

        override fun locationManager(
            manager: CLLocationManager,
            didUpdateLocations: List<*>
        ) {
            val location = didUpdateLocations.firstOrNull() as? CLLocation ?: return
            val coordinate = location.coordinate

            var latitude = 0.0
            var longitude = 0.0
            coordinate.useContents {
                latitude = this.latitude
                longitude = this.longitude
            }

            manager.stopUpdatingLocation()
            manager.delegate = null

            val geocoder = CLGeocoder()
            geocoder.reverseGeocodeLocation(location) { placemarks: List<*>?, _: NSError? ->
                val placemark = placemarks?.firstOrNull() as? CLPlacemark
                val address = if (placemark != null) {
                    "${placemark.subLocality ?: placemark.locality ?: ""}, ${placemark.administrativeArea ?: ""}"
                } else {
                    "$latitude, $longitude"
                }
                continuation.resume(
                    Result.success(
                        LocationData(latitude = latitude, longitude = longitude, address = address)
                    )
                )
            }
        }

        override fun locationManager(
            manager: CLLocationManager,
            didFailWithError: NSError
        ) {
            manager.stopUpdatingLocation()
            manager.delegate = null
            continuation.resume(Result.failure(Exception(didFailWithError.localizedDescription)))
        }
    }
}
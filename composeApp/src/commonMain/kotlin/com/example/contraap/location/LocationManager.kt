package com.example.contraap.location

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String
)

expect class LocationManager {
    fun getCurrentLocation(
        onSuccess: (LocationData) -> Unit,
        onError: (String) -> Unit
    )

    fun hasLocationPermission(): Boolean
}
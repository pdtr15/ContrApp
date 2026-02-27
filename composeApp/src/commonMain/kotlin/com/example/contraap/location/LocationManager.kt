package com.example.contraap.location

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String
)

expect class LocationManager() {

    suspend fun getCurrentLocation(): Result<LocationData>

    fun hasLocationPermission(): Boolean
}
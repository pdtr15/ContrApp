package com.example.contraap.viewmodel

import androidx.lifecycle.ViewModel
import com.example.contraap.location.LocationData
import com.example.contraap.location.LocationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LocationUiState(
    val currentLocation: LocationData? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val permissionGranted: Boolean = false
)

class LocationViewModel(
    private val locationManager: LocationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    init {
        checkPermission()
    }

    fun checkPermission() {
        _uiState.update {
            it.copy(permissionGranted = locationManager.hasLocationPermission())
        }
    }

    fun requestCurrentLocation() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        locationManager.getCurrentLocation(
            onSuccess = { location ->
                _uiState.update {
                    it.copy(
                        currentLocation = location,
                        isLoading = false,
                        error = null
                    )
                }
            },
            onError = { errorMessage ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            }
        )
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
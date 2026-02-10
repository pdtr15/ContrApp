package com.example.contraap.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class OnboardingUiState(
    val currentPage: Int = 0,
    val totalPages: Int = 3,
    val isLastPage: Boolean = false,
    val isOnboardingCompleted: Boolean = false
)

class OnboardingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onNextPage() {
        val currentPage = _uiState.value.currentPage
        val totalPages = _uiState.value.totalPages

        if (currentPage < totalPages - 1) {
            _uiState.update {
                it.copy(
                    currentPage = currentPage + 1,
                    isLastPage = currentPage + 1 == totalPages - 1
                )
            }
        } else {
            // Última página - completar onboarding
            _uiState.update { it.copy(isOnboardingCompleted = true) }
        }
    }

    fun onPreviousPage() {
        val currentPage = _uiState.value.currentPage
        if (currentPage > 0) {
            _uiState.update {
                it.copy(
                    currentPage = currentPage - 1,
                    isLastPage = false
                )
            }
        }
    }

    fun skipOnboarding() {
        _uiState.update { it.copy(isOnboardingCompleted = true) }
    }
}
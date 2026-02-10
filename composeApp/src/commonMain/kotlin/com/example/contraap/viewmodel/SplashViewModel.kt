package com.example.contraap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SplashUiState(
    val progress: Float = 0f,
    val isLoading: Boolean = true,
    val isCompleted: Boolean = false
)

class SplashViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        startSplashAnimation()
    }

    private fun startSplashAnimation() {
        viewModelScope.launch {
            val duration = 2500L
            val steps = 50
            val stepDelay = duration / steps

            repeat(steps) { step ->
                delay(stepDelay)
                val progress = (step + 1) / steps.toFloat()
                _uiState.update { it.copy(progress = progress) }
            }

            // Splash completado
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isCompleted = true
                )
            }
        }
    }
}
package com.example.contraap.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        // Validaciones
        if (_uiState.value.email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El email es requerido") }
            return
        }

        if (_uiState.value.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "La contraseña es requerida") }
            return
        }

        // Simulación de login (aquí iría tu lógica real)
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        // TODO: Implementar llamada a API/autenticación real
        // Por ahora, simulamos éxito
        _uiState.update {
            it.copy(
                isLoading = false,
                isLoginSuccessful = true
            )
        }
    }

    fun onGoogleLogin() {
        // TODO: Implementar login con Google
        println("Login with Google")
    }

    fun onFacebookLogin() {
        // TODO: Implementar login con Facebook
        println("Login with Facebook")
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun resetLoginSuccess() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }
}
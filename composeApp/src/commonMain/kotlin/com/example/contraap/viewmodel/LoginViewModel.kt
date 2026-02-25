package com.example.contraap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contraap.data.models.UserProfile
import com.example.contraap.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false,
    val currentUser: UserProfile? = null
)

class LoginViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            if (authRepository.isUserLoggedIn()) {
                authRepository.getCurrentUser().fold(
                    onSuccess = { user ->
                        _uiState.update {
                            it.copy(
                                currentUser = user,
                                isLoginSuccessful = user != null
                            )
                        }
                    },
                    onFailure = { }
                )
            }
        }
    }

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
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Por favor completa todos los campos")
            }
            return
        }

        if (!email.contains("@") || !email.contains(".")) {
            _uiState.update {
                it.copy(errorMessage = "Email inválido")
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            authRepository.signIn(email, password).fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccessful = true,
                            currentUser = user
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = when {
                                error.message?.contains("Invalid login credentials") == true ->
                                    "Email o contraseña incorrectos"
                                error.message?.contains("Email not confirmed") == true ->
                                    "Por favor confirma tu email"
                                else -> "Error al iniciar sesión: ${error.message}"
                            }
                        )
                    }
                }
            )
        }
    }

    fun onGoogleLogin() {
        // TODO: Implementar login con Google
        _uiState.update {
            it.copy(errorMessage = "Login con Google próximamente")
        }
    }

    fun onFacebookLogin() {
        // TODO: Implementar login con Facebook
        _uiState.update {
            it.copy(errorMessage = "Login con Facebook próximamente")
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun resetLoginSuccess() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }
}
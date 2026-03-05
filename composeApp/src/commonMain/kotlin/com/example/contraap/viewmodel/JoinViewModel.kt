package com.example.contraap.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contraap.data.models.UserRole
import com.example.contraap.data.repository.AuthRepository
import kotlinx.coroutines.launch

class JoinViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    // 🔥 ROL DINÁMICO
    var role by mutableStateOf<UserRole?>(null)

    // Estados del formulario
    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")
    var telefono by mutableStateOf("")
    var password by mutableStateOf("")

    var dpi by mutableStateOf("")

    // Control UI
    var mostrarDialogo by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var registroExitoso by mutableStateOf(false)

    fun onCrearCuenta() {

        if (role == null) {
            mensajeError = "Error interno: rol no definido"
            return
        }

        if (nombre.isBlank()) {
            mensajeError = "El nombre es requerido"
            return
        }

        if (correo.isBlank() || !correo.contains("@")) {
            mensajeError = "Ingresa un correo válido"
            return
        }

        if (telefono.isBlank()) {
            mensajeError = "El teléfono es requerido"
            return
        }

        if (password.length < 6) {
            mensajeError = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        isLoading = true
        mensajeError = null

        viewModelScope.launch {

            authRepository.signUp(
                email = correo.trim(),
                password = password,
                fullName = nombre.trim(),
                role = role!!   // 🔥 AQUÍ YA NO ES FIJO
            ).fold(

                onSuccess = { userProfile ->
                    updateUserPhone(userProfile.id)
                },

                onFailure = { error ->
                    isLoading = false
                    mensajeError = when {
                        error.message?.contains("already registered") == true ->
                            "Este email ya está registrado"
                        error.message?.contains("Invalid email") == true ->
                            "Email inválido"
                        error.message?.contains("Password") == true ->
                            "La contraseña debe tener al menos 6 caracteres"
                        else -> "Error al registrar: ${error.message}"
                    }
                }
            )
        }
    }

    private suspend fun updateUserPhone(userId: String) {

        authRepository.updateUserPhone(
            userId = userId,
            phone = telefono.trim()
        ).fold(
            onSuccess = {
                // Guardar DPI si fue ingresado
                if (dpi.isNotBlank()) {
                    authRepository.updateUserDocuments(
                        userId = userId,
                        dpi = dpi.trim()
                    )
                }
                isLoading = false
                registroExitoso = true
                mostrarDialogo = true
            },
            onFailure = {
                // Aunque falle el teléfono, intentar guardar DPI
                if (dpi.isNotBlank()) {
                    authRepository.updateUserDocuments(
                        userId = userId,
                        dpi = dpi.trim()
                    )
                }
                isLoading = false
                registroExitoso = true
                mostrarDialogo = true
            }
        )
    }

    fun limpiarError() {
        mensajeError = null
    }

    fun resetState() {
        nombre = ""
        correo = ""
        telefono = ""
        password = ""
        mostrarDialogo = false
        mensajeError = null
        isLoading = false
        registroExitoso = false
        role = null
    }
}
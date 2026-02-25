package com.example.contraap.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contraap.data.models.UserProfile
import com.example.contraap.data.models.UserRole
import com.example.contraap.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    // Estados observables
    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")
    var telefono by mutableStateOf("")
    var password by mutableStateOf("")
    var especialidadesSeleccionadas by mutableStateOf(listOf<String>())

    var mostrarDialogo by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var registroExitoso by mutableStateOf(false)

    fun onRegister() {
        // Validaciones
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

        if (especialidadesSeleccionadas.isEmpty()) {
            mensajeError = "Selecciona al menos una especialidad"
            return
        }

        // Registro
        isLoading = true
        mensajeError = null

        viewModelScope.launch {
            authRepository.signUp(
                email = correo.trim(),
                password = password,
                fullName = nombre.trim(),
                role = UserRole.CONTRATISTA
            ).fold(
                onSuccess = { userProfile ->
                    // Crear perfil de contratista
                    createContractorProfile(userProfile, especialidadesSeleccionadas.first())
                },
                onFailure = { error ->
                    isLoading = false
                    mensajeError = when {
                        error.message?.contains("already registered") == true ->
                            "Este email ya está registrado"
                        error.message?.contains("User already registered") == true ->
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

    private suspend fun createContractorProfile(userProfile: UserProfile, especialidad: String) {
        // Obtener el ID de la categoría según la especialidad
        val categoryId = when (especialidad) {
            "Servicio de limpieza" -> 1
            "Jardinería" -> 2
            "Electricista" -> 3
            "Plomería" -> 4
            "Mecánico Moto/Carros" -> 5
            "Tutorías (Matemáticas/ESL)" -> 6
            "Pintor" -> 7
            "Línea blanca" -> 8
            "Fumigadores" -> 9
            "Albañilería" -> 10
            else -> 1
        }

        authRepository.createContractorProfile(
            userId = userProfile.id,
            categoryId = categoryId,
            phone = telefono.trim()
        ).fold(
            onSuccess = {
                isLoading = false
                registroExitoso = true
                mostrarDialogo = true
            },
            onFailure = { error ->
                isLoading = false
                mensajeError = "Error al crear perfil de contratista: ${error.message}"
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
        especialidadesSeleccionadas = emptyList()
        mostrarDialogo = false
        mensajeError = null
        isLoading = false
        registroExitoso = false
    }
}
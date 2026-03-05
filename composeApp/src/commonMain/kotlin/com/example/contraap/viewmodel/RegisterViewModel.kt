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

    // 🔥 ROL DINÁMICO (AGREGAR ESTO)
    var role by mutableStateOf<UserRole?>(null)

    // Estados observables
    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")
    var telefono by mutableStateOf("")
    var password by mutableStateOf("")
    var especialidadesSeleccionadas by mutableStateOf(listOf<String>())

    var dpi by mutableStateOf("")

    var mostrarDialogo by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var registroExitoso by mutableStateOf(false)

    var documentoNombre by mutableStateOf<String?>(null)
    var documentoBytes by mutableStateOf<ByteArray?>(null)

    fun onRegister() {

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

        if (especialidadesSeleccionadas.isEmpty()) {
            mensajeError = "Selecciona al menos una especialidad"
            return
        }

        if (documentoBytes == null) {
            mensajeError = "Debes subir tu DPI o RTU"
            return
        }

        isLoading = true
        mensajeError = null

        viewModelScope.launch {

            authRepository.signUp(
                email = correo.trim(),
                password = password,
                fullName = nombre.trim(),
                role = role!!  // 🔥 YA NO HARDCODEADO
            ).fold(

                onSuccess = { userProfile ->
                    createContractorProfile(
                        userProfile,
                        especialidadesSeleccionadas.first()
                    )
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

    private suspend fun createContractorProfile(
        userProfile: UserProfile,
        especialidad: String
    ) {

        val categoryId = when (especialidad) {
            "Servicio de limpieza" -> 1
            "Jardinería" -> 2
            "Electricista" -> 3
            "Plomería" -> 4
            "Mecánico Moto/Carros" -> 5
            "Tutorías" -> 6
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
                // Subir documento a Storage si existe y obtener URL
                var documentUrl: String? = null
                val bytes = documentoBytes
                val nombre = documentoNombre
                if (bytes != null && nombre != null) {
                    authRepository.uploadDocument(
                        userId = userProfile.id,
                        fileName = nombre,
                        bytes = bytes
                    ).fold(
                        onSuccess = { url -> documentUrl = url },
                        onFailure = { /* continuar sin URL */ }
                    )
                }

                // Guardar DPI y URL siempre que haya al menos uno
                authRepository.updateUserDocuments(
                    userId = userProfile.id,
                    dpi = dpi.trim().takeIf { it.isNotBlank() },
                    documentUrl = documentUrl
                )

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
        role = null
    }
}
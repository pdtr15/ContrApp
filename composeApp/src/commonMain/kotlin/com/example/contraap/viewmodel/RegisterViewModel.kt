package com.example.contraap.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class RegisterViewModel {
    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")
    var telefono by mutableStateOf("")
    var password by mutableStateOf("")
    var especialidadesSeleccionadas by mutableStateOf(listOf<String>())
    var mostrarDialogo by mutableStateOf(false)

    fun onRegister() {
        // Aquí validas y guardas
        mostrarDialogo = true
    }
}
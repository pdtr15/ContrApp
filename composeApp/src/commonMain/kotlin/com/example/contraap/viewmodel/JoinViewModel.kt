package com.example.contraap.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class JoinViewModel {
    // Estados específicos para el registro de usuario general
    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")

    var telefono by mutableStateOf("")
    var password by mutableStateOf("")

    // Control de UI
    var mostrarDialogo by mutableStateOf(false)

    fun onCrearCuenta() {
        if (nombre.isNotBlank() && correo.contains("@") && password.length >= 6) {
            // Aquí simularías la llamada al servicio de autenticación
            println("Creando cuenta de usuario: $nombre ($correo)")
            mostrarDialogo = true
        } else {
            println("Error: Datos incompletos o inválidos")
        }
    }
}
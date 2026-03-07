package com.example.contraap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.contraap.data.models.UserRole
import com.example.contraap.ui.components.*
import com.example.contraap.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    role: UserRole,
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {

    val viewModel: RegisterViewModel = viewModel()

    // 🔥 ASIGNAR ROL AL VIEWMODEL
    LaunchedEffect(Unit) {
        viewModel.role = role
    }

    LaunchedEffect(viewModel.registroExitoso) {
        if (viewModel.registroExitoso) {
            onRegisterSuccess()
        }
    }

    val especialidades = listOf(
        "Servicio de limpieza 🧹",
        "Jardinería 🌿",
        "Electricista ⚡",
        "Plomería 🔧",
        "Pintor 🎨",
        "Albañilería 🧱"
    )

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Registro Profesional",
                onBackClick = onBack
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            CustomOutlinedTextField(
                value = viewModel.nombre,
                onValueChange = { viewModel.nombre = it },
                label = "Nombre Completo",
                icon = Icons.Default.Person
            )

            CustomOutlinedTextField(
                value = viewModel.correo,
                onValueChange = { viewModel.correo = it },
                label = "Correo",
                icon = Icons.Default.Email
            )

            PhoneTextField(
                value = viewModel.telefono,
                onValueChange = { viewModel.telefono = it },
                label = "Teléfono",
                icon = Icons.Default.Phone
            )

            PasswordTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = "Contraseña"
            )

            CustomDropdownMenu(
                label = "Especialidades",
                placeholder = "Selecciona tus oficios",
                opciones = especialidades,
                selectedItems = viewModel.especialidadesSeleccionadas,
                onSelectionChange = {
                    viewModel.especialidadesSeleccionadas = it
                }
            )

            NumericTextField(
                value = viewModel.dpi,
                onValueChange = { viewModel.dpi = it },
                label = "DPI",
                icon = Icons.Default.Badge,
                maxLength = 13
            )

            DocumentPickerBox(
                selectedFileName = viewModel.documentoNombre,
                onFileSelected = { name, bytes ->
                    viewModel.documentoNombre = name
                    viewModel.documentoBytes = bytes
                }
            )

            if (viewModel.mensajeError != null) {
                Text(
                    text = viewModel.mensajeError!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            CustomButton(
                text = if (viewModel.isLoading) "Registrando..." else "Registrarse",
                onClick = { viewModel.onRegister() },
                enabled = !viewModel.isLoading
            )
        }
    }
}
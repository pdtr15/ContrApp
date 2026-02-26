package com.example.contraap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
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
import com.example.contraap.viewmodel.JoinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinContrAppScreen(
    role: UserRole,
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {

    val viewModel: JoinViewModel = viewModel()

    // 🔥 ASIGNAR ROL
    LaunchedEffect(Unit) {
        viewModel.role = role
    }

    LaunchedEffect(viewModel.registroExitoso) {
        if (viewModel.registroExitoso) {
            onRegisterSuccess()
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Registro Cliente",
                onBackClick = onBack
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            CustomOutlinedTextField(
                value = viewModel.nombre,
                onValueChange = { viewModel.nombre = it },
                label = "Nombre",
                icon = Icons.Default.Person
            )

            CustomOutlinedTextField(
                value = viewModel.correo,
                onValueChange = { viewModel.correo = it },
                label = "Correo",
                icon = Icons.Default.Email
            )

            CustomOutlinedTextField(
                value = viewModel.telefono,
                onValueChange = { viewModel.telefono = it },
                label = "Teléfono",
                icon = Icons.Default.Phone
            )

            CustomOutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = "Contraseña",
                icon = Icons.Default.Visibility,
                isPassword = true
            )

            if (viewModel.mensajeError != null) {
                Text(
                    text = viewModel.mensajeError!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            CustomButton(
                text = if (viewModel.isLoading) "Creando cuenta..." else "Crear Cuenta",
                onClick = { viewModel.onCrearCuenta() },
                enabled = !viewModel.isLoading
            )
        }
    }
}
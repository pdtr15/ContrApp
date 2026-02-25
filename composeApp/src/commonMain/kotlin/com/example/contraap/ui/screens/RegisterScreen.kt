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
import com.example.contraap.ui.components.CustomButton
import com.example.contraap.ui.components.CustomDropdownMenu
import com.example.contraap.ui.components.CustomOutlinedTextField
import com.example.contraap.ui.components.CustomTopAppBar
import com.example.contraap.viewmodel.RegisterViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit = {}
) {
    val viewModel: RegisterViewModel = viewModel()

    val especialidades = listOf(
        "Servicio de limpieza",
        "Jardinería",
        "Electricista",
        "Plomería",
        "Mecánico Moto/Carros",
        "Tutorías (Matemáticas/ESL)",
        "Pintor",
        "Línea blanca",
        "Fumigadores",
        "Albañilería"
    )

    // Navegar cuando el registro es exitoso
    LaunchedEffect(viewModel.registroExitoso) {
        if (viewModel.registroExitoso) {
            onRegisterSuccess()
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Crear Cuenta",
                onBackClick = { onBack() }
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
            Text(
                text = "Registro de Profesional",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Únete a nuestra red de expertos.",
                color = Color(0xFF1976D2)
            )

            CustomOutlinedTextField(
                value = viewModel.nombre,
                onValueChange = { viewModel.nombre = it },
                label = "Nombre Completo",
                placeholder = "Ej. Juan Pérez",
                icon = Icons.Default.Person
            )

            CustomOutlinedTextField(
                value = viewModel.correo,
                onValueChange = { viewModel.correo = it },
                label = "Correo Electrónico",
                placeholder = "nombre@ejemplo.com",
                icon = Icons.Default.Email
            )

            CustomOutlinedTextField(
                value = viewModel.telefono,
                onValueChange = { viewModel.telefono = it },
                label = "Teléfono",
                placeholder = "+502 0000 0000",
                icon = Icons.Default.Phone
            )

            CustomOutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = "Contraseña",
                icon = Icons.Default.Visibility,
                placeholder = "***********",
                isPassword = true
            )

            CustomDropdownMenu(
                label = "Especialidad",
                placeholder = "Selecciona tus oficios",
                opciones = especialidades,
                selectedItems = viewModel.especialidadesSeleccionadas,
                onSelectionChange = { nuevaLista ->
                    viewModel.especialidadesSeleccionadas = nuevaLista
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(1.dp, Color(0xFF64B5F6), RoundedCornerShape(12.dp))
                    .background(Color(0xFFE3F2FD), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = Color(0xFF1976D2)
                    )
                    Text("Sube tu DPI o RTU aquí")
                    Text("PDF, JPG o PNG (Max. 5MB)", fontSize = 12.sp)
                }
            }

            // Mostrar error si existe
            if (viewModel.mensajeError != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = viewModel.mensajeError!!,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            CustomButton(
                text = if (viewModel.isLoading) "Registrando..." else "Registrarse",
                onClick = { viewModel.onRegister() },
                icon = Icons.Default.ArrowForward,
                buttonColor = Color(0xFFFFC107),
                textColor = Color.Black,
                iconTint = Color.Black,
                height = 56.dp,
                cornerRadius = 12.dp,
                enabled = !viewModel.isLoading
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("¿Ya tienes una cuenta? ")
                Text(
                    text = "Inicia sesión",
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onBack() }
                )
            }
        }
    }

    // Modal de confirmación
    if (viewModel.mostrarDialogo) {
        AlertDialog(
            onDismissRequest = {
                viewModel.mostrarDialogo = false
                onRegisterSuccess()
            },
            title = { Text("¡Registro Exitoso!") },
            text = { Text("Tu cuenta ha sido creada correctamente. Bienvenido a ContrApp.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.mostrarDialogo = false
                        onRegisterSuccess()
                    }
                ) {
                    Text("Continuar")
                }
            }
        )
    }
}
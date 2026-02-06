package com.example.contraap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
// IMPORTANTE: Verifica que estos nombres de paquete sean correctos según tus carpetas
import com.example.contraap.ui.components.CustomOutlinedTextField
import com.example.contraap.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    // Creamos una instancia del ViewModel persistente en este Composable
    val viewModel = remember { RegisterViewModel() }
    val especialidades = listOf("Electricista", "Plomero", "Carpintero", "Albañil", "Pintor", "Jardinero")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                // Agregamos scroll por si el teclado tapa los campos
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Registro de Profesional", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "Únete a nuestra red de expertos.", color = Color(0xFF1976D2))

            // USANDO TU COMPONENTE CustomOutlinedTextField
            CustomOutlinedTextField(
                value = viewModel.nombre,
                onValueChange = { viewModel.nombre = it },
                label = "Nombre Completo",
                icon = Icons.Default.Person
            )

            CustomOutlinedTextField(
                value = viewModel.correo,
                onValueChange = { viewModel.correo = it },
                label = "Correo Electrónico",
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

            // DROPDOWN DE ESPECIALIDAD
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = viewModel.especialidad,
                    onValueChange = {},
                    label = { Text("Especialidad") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    especialidades.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                viewModel.especialidad = opcion
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Subida de archivo (Borde sólido por ahora para que no te de error)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(1.dp, Color(0xFF64B5F6), RoundedCornerShape(12.dp))
                    .background(Color(0xFFE3F2FD), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color(0xFF1976D2))
                    Text("Sube tu DPI o RTU aquí")
                    Text("PDF, JPG o PNG (Max. 5MB)", fontSize = 12.sp)
                }
            }

            Button(
                onClick = { viewModel.onRegister() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
            ) {
                Text("Registrarse", color = Color.Black)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.Black)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("¿Ya tienes una cuenta? ")
                Text(text = "Inicia sesión", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
            }
        }
    }

    // MODAL DE CONFIRMACIÓN USANDO EL ESTADO DEL VIEWMODEL
    if (viewModel.mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { viewModel.mostrarDialogo = false },
            title = { Text("Registro") },
            text = { Text("Usuario registrado correctamente") },
            confirmButton = {
                TextButton(onClick = { viewModel.mostrarDialogo = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}
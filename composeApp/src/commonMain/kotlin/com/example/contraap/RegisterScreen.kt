import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {

    // Estados de los campos
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Dropdown Especialidad
    var especialidadSeleccionada by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val especialidades = listOf(
        "Electricista",
        "Plomero",
        "Carpintero",
        "Albañil",
        "Pintor",
        "Jardinero"
    )

    // Control del modal
    var mostrarDialogo by remember { mutableStateOf(false) }

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
                .padding(16.dp),
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

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                placeholder = { Text("Ej. Juan Pérez") },
                trailingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                placeholder = { Text("nombre@ejemplo.com") },
                trailingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                placeholder = { Text("+502 0000 0000") },
                trailingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                placeholder = { Text("********") },
                trailingIcon = {
                    Icon(Icons.Default.Visibility, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )

            // DROPDOWN DE ESPECIALIDAD
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = especialidadSeleccionada,
                    onValueChange = {},
                    label = { Text("Especialidad") },
                    placeholder = { Text("Selecciona tu oficio") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    especialidades.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                especialidadSeleccionada = opcion
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Simulación subida archivo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF64B5F6),
                        shape = RoundedCornerShape(12.dp)
                    )
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

            Button(
                onClick = {

                    // Mostrar datos en consola
                    println("===== DATOS REGISTRO =====")
                    println("Nombre: $nombre")
                    println("Correo: $correo")
                    println("Teléfono: $telefono")
                    println("Contraseña: $password")
                    println("Especialidad: $especialidadSeleccionada")

                    // Mostrar modal
                    mostrarDialogo = true

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC107)
                )
            ) {
                Text("Registrarse →", color = Color.Black)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("¿Ya tienes una cuenta? ")
                Text(
                    text = "Inicia sesión",
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // MODAL DE CONFIRMACIÓN
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Registro") },
            text = { Text("Usuario registrado correctamente") },
            confirmButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

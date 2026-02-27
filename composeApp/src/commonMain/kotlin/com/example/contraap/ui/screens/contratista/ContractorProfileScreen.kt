package com.example.contraap.ui.screens.contractor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightBackground = Color(0xFFF5F7FA)
private val CardBlue        = Color(0xFF4FC3F7)
private val ButtonYellow    = Color(0xFFFFCA28)
private val TextDark        = Color(0xFF2D3142)
private val TextGray        = Color(0xFF9098B1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractorProfileScreen() {

    var showEditProfile         by remember { mutableStateOf(false) }
    var showNotifications       by remember { mutableStateOf(false) }

    val unreadCount = 2 // puedes conectar esto a datos reales después

    when {

        showEditProfile -> {
            ContractorEditProfileScreen(
                onBackClick = { showEditProfile = false }
            )
        }

        showNotifications -> {
            ContractorNotificationsScreen(
                onBackClick = { showNotifications = false }
            )
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightBackground)
            ) {
                // ── HEADER ────────────────────────────────────────────────────
                Surface(
                    shadowElevation = 4.dp,
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { showEditProfile = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(55.dp),
                                shape = CircleShape,
                                color = CardBlue.copy(alpha = 0.2f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "JP",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = CardBlue
                                    )
                                }
                            }

                            Spacer(Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "¡Hola Juan Pérez!",
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                                Text(
                                    text = "Editar mi perfil",
                                    color = TextGray,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        // Campana de notificaciones
                        IconButton(onClick = { showNotifications = true }) {
                            BadgedBox(
                                badge = {
                                    if (unreadCount > 0) {
                                        Badge(containerColor = ButtonYellow) {
                                            Text(unreadCount.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notificaciones",
                                    tint = TextDark
                                )
                            }
                        }
                    }
                }

                // ── CONTENIDO ─────────────────────────────────────────────────
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {
                        ContractorSettingsSection("Mi Negocio") {
                            ContractorSettingsItem(Icons.Default.Build,      "Mis Especialidades")
                            ContractorSettingsItem(Icons.Default.Schedule,   "Horarios de Trabajo")
                            ContractorSettingsItem(Icons.Default.LocationOn, "Radio de Servicio")
                            ContractorSettingsItem(Icons.Default.Star,       "Mis Reseñas")
                        }
                    }

                    item {
                        ContractorSettingsSection("Pagos") {
                            ContractorSettingsItem(Icons.Default.AccountBalance, "Cuentas Bancarias")
                            ContractorSettingsItem(Icons.Default.Receipt,        "Historial de Pagos")
                        }
                    }

                    item {
                        ContractorSettingsSection("Preferencias") {
                            ContractorSettingsItem(Icons.Default.Notifications, "Configurar Notificaciones")
                            ContractorSettingsItem(Icons.Default.Security,      "Privacidad y Seguridad")
                            ContractorSettingsItem(Icons.Default.Language,      "Idioma")
                        }
                    }

                    item {
                        ContractorSettingsSection("Ayuda") {
                            ContractorSettingsItem(Icons.Default.Help,   "Centro de Ayuda")
                            ContractorSettingsItem(Icons.Default.Policy, "Políticas de privacidad")
                            ContractorSettingsItem(Icons.Default.Badge,  "Verificación de cuenta")
                        }
                    }

                    item {
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE53935)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.ExitToApp, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Cerrar sesión", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    item {
                        Text(
                            text = "Versión 1.0.0",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = TextGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

// ── Sección de settings ───────────────────────────────────────────────────────
@Composable
fun ContractorSettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = TextGray,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                content()
            }
        }
    }
}

// ── Item individual ───────────────────────────────────────────────────────────
@Composable
fun ContractorSettingsItem(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = CardBlue,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            color = TextDark,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextGray,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ── Pantalla editar perfil ────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractorEditProfileScreen(onBackClick: () -> Unit) {
    var name        by remember { mutableStateOf("Juan Pérez") }
    var email       by remember { mutableStateOf("juan@example.com") }
    var phone       by remember { mutableStateOf("+52 55 1234 5678") }
    var specialty   by remember { mutableStateOf("Plomero Profesional") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = LightBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Cuenta", fontWeight = FontWeight.Bold, color = TextGray)

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = specialty,
                onValueChange = { specialty = it },
                label = { Text("Especialidad") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { onBackClick() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CardBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar cambios", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(Modifier.height(8.dp))

            Text("Seguridad", fontWeight = FontWeight.Bold, color = TextGray)

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cambiar contraseña")
            }

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE53935))
            ) {
                Text("Eliminar mi cuenta")
            }
        }
    }
}

// ── Pantalla notificaciones ───────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractorNotificationsScreen(onBackClick: () -> Unit) {
    val notifications = remember {
        listOf(
            "Nuevo trabajo disponible cerca de ti" to "hace 5 min",
            "Juan Pérez ha dejado una reseña" to "hace 1 hora",
            "Tu pago de \$85.00 fue procesado" to "hace 2 días"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = LightBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            notifications.forEach { (message, time) ->
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(CardBlue.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = CardBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(message, fontSize = 14.sp, color = TextDark, fontWeight = FontWeight.Medium)
                                Text(time, fontSize = 12.sp, color = TextGray)
                            }
                        }
                    }
                }
            }
        }
    }
}
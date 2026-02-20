package com.example.contraap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contraap.ui.theme.*
import androidx.compose.ui.graphics.Color

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false
)

data class SavedCard(
    val id: String,
    val holderName: String,
    val last4: String,
    val brand: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {

    var showEditProfile by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    var showNotificationSettings by remember { mutableStateOf(false) }

    val notifications = remember {
        listOf(
            Notification(
                "1",
                "Solicitud 12345",
                "Nuestro contratista te visitará muy pronto",
                "hace 2 días",
                false
            ),
            Notification(
                "2",
                "Solictud terminada",
                "!El contratista ha terminado el trabajo!",
                "hace 2 días",
                false
            )
        )
    }

    when {
        showEditProfile -> {
            EditProfileScreen { showEditProfile = false }
        }
        showNotifications -> {
            NotificationsScreen(
                notifications = notifications,
                onBackClick = { showNotifications = false },
                onConfigureClick = {
                    showNotifications = false
                    showNotificationSettings = true
                }
            )
        }
        showNotificationSettings -> {
            NotificationSettingsScreen {
                showNotificationSettings = false
            }
        }
        else -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundWhite)
            ) {

                // HEADER
                Surface(
                    shadowElevation = 4.dp,
                    color = BackgroundWhite
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
                                color = AccentYellow.copy(alpha = 0.2f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "P",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        color = AccentYellow
                                    )
                                }
                            }

                            Spacer(Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "¡Hola pablo tzul!",
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Editar mi perfil",
                                    color = TextSecondary
                                )
                            }
                        }

                        IconButton(onClick = { showNotifications = true }) {
                            BadgedBox(
                                badge = {
                                    if (notifications.any { !it.isRead }) {
                                        Badge(
                                            containerColor = AccentYellow
                                        ) {
                                            Text(
                                                notifications.count { !it.isRead }.toString()
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = TextPrimary
                                )
                            }
                        }
                    }
                }

                // CONTENIDO
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {
                        SettingsSection("Preferencias") {
                            SettingsItem(Icons.Default.Language, "Idioma")
                            SettingsItem(Icons.Default.CreditCard, "Tarjetas guardadas")
                            SettingsItem(Icons.Default.DarkMode, "Tema")
                        }
                    }

                    item {
                        SettingsSection("Ayuda") {
                            SettingsItem(Icons.Default.Help, "Ayuda")
                            SettingsItem(Icons.Default.Policy, "Políticas de privacidad")
                        }
                    }

                    item {
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentYellow
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
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BackgroundWhite)
        ) {
            Column { content() }
        }
    }
}

@Composable
fun SettingsItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = AccentYellow)
        Spacer(Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    notifications: List<Notification>,
    onBackClick: () -> Unit,
    onConfigureClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AccentYellow
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Button(
                onClick = onConfigureClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentYellow
                ),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text("Configurar")
            }

            LazyColumn {

                items(notifications) { notification ->
                    ListItem(
                        headlineContent = { Text(notification.title) },
                        supportingContent = {
                            Column {
                                Text(notification.message)
                                Text(
                                    notification.time,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    )
                    Divider()
                }

                item {
                    Text(
                        "🎉 Llegaste al final. Todas las notificaciones de hace más de 1 mes son borradas automáticamente",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onBackClick: () -> Unit
) {

    val sections = listOf(
        "Mis Solicitudes",
        "Respuestas",
        "Mensajes"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AccentYellow,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        },
        containerColor = BackgroundWhite
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(sections) { section ->

                var pushEnabled by remember { mutableStateOf(true) }
                var emailEnabled by remember { mutableStateOf(true) }

                Card(
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BackgroundWhite
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {

                        Text(
                            text = section,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            NotificationToggle(
                                title = "Push",
                                checked = pushEnabled,
                                onCheckedChange = { pushEnabled = it }
                            )

                            NotificationToggle(
                                title = "E-mail",
                                checked = emailEnabled,
                                onCheckedChange = { emailEnabled = it }
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun NotificationToggle(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(Modifier.height(8.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = BackgroundWhite,
                checkedTrackColor = AccentYellow,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE0E0E0)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit
) {

    var firstName by remember { mutableStateOf("Pablo Daniel") }
    var lastName by remember { mutableStateOf("Tzul") }
    var email by remember { mutableStateOf("pablodtmr@gmail.com") }
    var phone by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Perfil y contraseña",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AccentYellow,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Spacer(Modifier.height(8.dp))

            Text("Cuenta", fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nombres *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellidos *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico para facturas *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono *") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentYellow
                )
            ) {
                Text("Guardar cambios")
            }

            Spacer(Modifier.height(16.dp))

            Text("Seguridad", fontWeight = FontWeight.Bold)

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cambiar contraseña")
            }

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar mi cuenta")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedCardsScreen(
    onBackClick: () -> Unit,
    onAddCardClick: () -> Unit
) {

    var cards by remember {
        mutableStateOf(
            listOf(
                SavedCard("1", "Pablo Daniel", "4242", "Visa"),
                SavedCard("2", "Pablo Daniel", "1881", "Mastercard")
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mis tarjetas",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AccentYellow,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddCardClick,
                containerColor = AccentYellow
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        containerColor = BackgroundWhite
    ) { padding ->

        if (cards.isEmpty()) {

            // Estado vacío elegante
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.CreditCard,
                    contentDescription = null,
                    tint = AccentYellow,
                    modifier = Modifier.size(60.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "No tienes tarjetas guardadas",
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Agrega una tarjeta para pagar más rápido",
                    color = TextSecondary
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onAddCardClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentYellow
                    )
                ) {
                    Text("Agregar tarjeta")
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(cards) { card ->

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = BackgroundWhite
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column {

                                Text(
                                    card.brand,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    "**** **** **** ${card.last4}",
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    card.holderName,
                                    color = TextSecondary,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            IconButton(
                                onClick = {
                                    cards = cards.filter { it.id != card.id }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = AccentYellow
                                )
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

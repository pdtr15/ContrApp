package com.example.contraap.ui.screens.contractor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contraap.ui.navigation.Routes
import com.example.contraap.location.LocationManager
import com.example.contraap.location.RequestLocationPermission
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import kotlinx.coroutines.launch

private val LightBackground = Color(0xFFF5F7FA)
private val CardBlue        = Color(0xFF4FC3F7)
private val ButtonYellow    = Color(0xFFFFCA28)
private val TextDark        = Color(0xFF2D3142)
private val TextGray        = Color(0xFF9098B1)
private val GreenOnline     = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractorDashboardScreen(navController: NavController) {
    val locationManager = remember { LocationManager() }

    var isOnline by remember { mutableStateOf(true) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf("Detectando ubicación...") }
    var shouldRequestPermission by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Si necesita pedir permiso, muestra el diálogo del sistema
    if (shouldRequestPermission) {
        RequestLocationPermission { granted: Boolean ->
            shouldRequestPermission = false
            if (granted) {
                scope.launch {
                    locationManager.getCurrentLocation().fold(
                        onSuccess = { locationData -> currentLocation = locationData.address },
                        onFailure = { currentLocation = "Sin ubicación configurada" }
                    )
                }
            } else {
                currentLocation = "Sin ubicación configurada"
            }
        }
    }

    // Al entrar: si ya tiene permiso obtiene ubicación, si no la pide
    LaunchedEffect(Unit) {
        if (locationManager.hasLocationPermission()) {
            locationManager.getCurrentLocation().fold(
                onSuccess = { locationData -> currentLocation = locationData.address },
                onFailure = { currentLocation = "Sin ubicación configurada" }
            )
        } else {
            shouldRequestPermission = true
        }
    }

    Scaffold(
        containerColor = LightBackground,
        topBar = { DashboardTopBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            FinancialCardsRow()
            ServiceStatusCard(isOnline = isOnline, onStatusChange = { isOnline = it })

            // ← NUEVA SECCIÓN DE UBICACIÓN
            LocationCard(
                currentLocation = currentLocation,
                onEditClick = { showLocationDialog = true }
            )

            NewRequestsSection(navController = navController)
            UpcomingAgendaSection()
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // ← DIALOG PARA EDITAR UBICACIÓN
    if (showLocationDialog) {
        LocationDialog(
            currentLocation = currentLocation,
            locationManager = locationManager,
            onDismiss = { showLocationDialog = false },
            onConfirm = { newLocation ->
                currentLocation = newLocation
                showLocationDialog = false
            },
            onRequestPermission = {
                showLocationDialog = false
                shouldRequestPermission = true
            }
        )
    }
}

// ── Card de ubicación ─────────────────────────────────────────────────────────
@Composable
fun LocationCard(
    currentLocation: String,
    onEditClick: () -> Unit
) {
    val sinUbicacion = currentLocation == "Sin ubicación configurada"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono izquierda
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (sinUbicacion) TextGray.copy(alpha = 0.1f)
                        else CardBlue.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = if (sinUbicacion) TextGray else CardBlue,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Texto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Mi Ubicación",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    currentLocation,
                    fontSize = 12.sp,
                    color = if (sinUbicacion) TextGray else CardBlue,
                    fontWeight = if (sinUbicacion) FontWeight.Normal else FontWeight.Medium
                )
            }

            // Botón editar
            TextButton(onClick = onEditClick) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null,
                    tint = ButtonYellow,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    if (sinUbicacion) "Agregar" else "Editar",
                    color = ButtonYellow,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ── Dialog para escribir ubicación ───────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDialog(
    currentLocation: String,
    locationManager: LocationManager,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    onRequestPermission: () -> Unit
) {
    var text by remember {
        mutableStateOf(
            if (currentLocation == "Sin ubicación configurada" ||
                currentLocation == "Detectando ubicación...") "" else currentLocation
        )
    }
    var gpsLoading by remember { mutableStateOf(false) }
    var gpsError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        icon = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(CardBlue.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = CardBlue,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        title = {
            Text(
                "Mi Ubicación",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextDark
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Ingresa tu dirección o zona donde ofreces tus servicios",
                    fontSize = 13.sp,
                    color = TextGray
                )

                // Botón GPS
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !gpsLoading) {
                            gpsError = null
                            if (!locationManager.hasLocationPermission()) {
                                onRequestPermission()
                            } else {
                                gpsLoading = true
                                scope.launch {
                                    locationManager.getCurrentLocation().fold(
                                        onSuccess = { locationData ->
                                            gpsLoading = false
                                            text = locationData.address
                                        },
                                        onFailure = { error ->
                                            gpsLoading = false
                                            gpsError = error.message ?: "Error desconocido"
                                        }
                                    )
                                }
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardBlue.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (gpsLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = CardBlue,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = CardBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                "Usar mi ubicación actual",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                if (gpsLoading) "Obteniendo ubicación GPS..." else "Detectar automáticamente",
                                fontSize = 11.sp,
                                color = TextGray
                            )
                        }
                    }
                }

                if (gpsError != null) {
                    Text(
                        text = gpsError!!,
                        color = Color(0xFFE53935),
                        fontSize = 11.sp
                    )
                }

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Ej: Colonia Roma Norte, CDMX") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = CardBlue
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CardBlue,
                        unfocusedBorderColor = TextGray.copy(alpha = 0.4f)
                    )
                )

                // Sugerencias rápidas
                Text("Sugerencias", fontSize = 11.sp, color = TextGray, fontWeight = FontWeight.Bold)
                listOf(
                    "Ciudad de México, CDMX",
                    "Guadalajara, Jalisco",
                    "Monterrey, Nuevo León"
                ).forEach { suggestion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightBackground, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Place,
                            contentDescription = null,
                            tint = TextGray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            suggestion,
                            fontSize = 13.sp,
                            color = TextDark,
                            modifier = Modifier
                                .weight(1f)
                                .noRippleClickable { text = suggestion }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (text.isNotBlank()) onConfirm(text) },
                colors = ButtonDefaults.buttonColors(containerColor = CardBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Guardar ubicación", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextGray)
            }
        }
    )
}

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = this.clickable(
    indication = null,
    interactionSource = remember { MutableInteractionSource() },
    onClick = onClick
)

// ── TopBar ────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CardBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Build,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "ContrApp",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextDark
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                BadgedBox(
                    badge = { Badge(containerColor = Color.Red) { Text("1") } }
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notificaciones",
                        tint = TextDark
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground)
    )
}

@Composable
fun FinancialCardsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Icon(Icons.Default.Payments, contentDescription = null, tint = CardBlue, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("INGRESOS HOY", fontSize = 10.sp, color = TextGray, fontWeight = FontWeight.Bold)
                Text("$150.00", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextDark)
            }
        }
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBlue),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = ButtonYellow, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("BALANCE TOTAL", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                Text("$1,240.50", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun ServiceStatusCard(isOnline: Boolean, onStatusChange: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Estado del Servicio", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Actualmente estás ", fontSize = 12.sp, color = TextGray)
                    Text(
                        if (isOnline) "En línea" else "Desconectado",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isOnline) GreenOnline else TextGray
                    )
                }
            }
            Switch(
                checked = isOnline,
                onCheckedChange = onStatusChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = GreenOnline
                )
            )
        }
    }
}

@Composable
fun NewRequestsSection(navController: NavController) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.FlashOn, contentDescription = null, tint = CardBlue, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nuevas Solicitudes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
            }
            Box(
                modifier = Modifier
                    .background(CardBlue.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text("3 nuevas", fontSize = 12.sp, color = CardBlue, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Reparación de Plomería", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Text("Juan Pérez • 3.2 km", fontSize = 13.sp, color = TextGray)
                    }
                    Text("$85.00", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CardBlue)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { navController.navigate(Routes.JOB_REQUEST_DETAIL) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonYellow),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = null, tint = TextDark, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ver Detalles", color = TextDark, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE53935))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Rechazar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun UpcomingAgendaSection() {
    Column {
        Text("Próxima Agenda", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Spacer(modifier = Modifier.height(12.dp))

        AgendaItemCard(
            date = "OCT\n24",
            time = "02:00 PM",
            title = "Cita de Servicio",
            statusText = "CONFIRMADO",
            statusColor = Color(0xFF4CAF50)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(40.dp).background(LightBackground, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = TextGray)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Juan Pérez", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Text("Reparación Eléctrica", fontSize = 12.sp, color = TextGray)
                }
                Text("Ver Detalles", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = CardBlue)
            }
        }
    }
}

@Composable
fun AgendaItemCard(
    date: String,
    time: String,
    title: String,
    statusText: String,
    statusColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(LightBackground, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(date, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray, textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(time, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Text(title, fontSize = 12.sp, color = TextGray)
            }
            Text(statusText, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = statusColor)
        }
    }
}
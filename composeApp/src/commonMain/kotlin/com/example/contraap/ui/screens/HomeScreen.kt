package com.example.contraap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contraap.ui.theme.*
import com.example.contraap.location.LocationManager
import com.example.contraap.location.RequestLocationPermission

data class Category(val id: Int, val name: String, val icon: String)
data class Contractor(
    val id: Int,
    val name: String,
    val profession: String,
    val rating: Double,
    val jobs: Int,
    val verified: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onRequestServiceClick: () -> Unit = {}
) {
    val locationManager = remember { LocationManager() }

    var currentLocation by remember { mutableStateOf("Detectando ubicación...") }
    var showLocationDialog by remember { mutableStateOf(false) }
    var loadingLocation by remember { mutableStateOf(false) }
    var shouldRequestPermission by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Función reutilizable para obtener ubicación
    suspend fun fetchLocation() {
        loadingLocation = true
        locationManager.getCurrentLocation().fold(
            onSuccess = { locationData ->
                currentLocation = locationData.address
                loadingLocation = false
            },
            onFailure = {
                currentLocation = "Ubicación no disponible"
                loadingLocation = false
            }
        )
    }

    // Si necesita pedir permiso, muestra el diálogo del sistema
    if (shouldRequestPermission) {
        RequestLocationPermission { granted: Boolean ->
            shouldRequestPermission = false
            if (granted) {
                scope.launch { fetchLocation() }
            } else {
                currentLocation = "Sin permiso de ubicación"
            }
        }
    }

    // Al entrar: si ya tiene permiso obtiene ubicación, si no la pide
    LaunchedEffect(Unit) {
        if (locationManager.hasLocationPermission()) {
            fetchLocation()
        } else {
            shouldRequestPermission = true
        }
    }

    val categories = remember {
        listOf(
            Category(1, "Servicio de limpieza", "🧹"),
            Category(2, "Jardinería", "🌿"),
            Category(3, "Electricista", "⚡"),
            Category(4, "Plomería", "🔧"),
            Category(5, "Pintor", "🎨"),
            Category(6, "Albañilería", "🧱")
        )
    }

    val topContractors = remember {
        listOf(
            Contractor(1, "Juan Pérez", "Plomería", 4.8, 127, true),
            Contractor(2, "María García", "Electricista", 4.9, 203, true)
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onRequestServiceClick,
                containerColor = AccentYellow,
                contentColor = TextPrimary,
                shape = RoundedCornerShape(Dimensions.paddingMedium())
            ) {
                if (Dimensions.isCompact()) {
                    // Solo ícono en pantallas pequeñas
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Nueva solicitud",
                        modifier = Modifier.size(Dimensions.iconSizeMedium())
                    )
                } else {
                    // Ícono + texto en pantallas grandes
                    Row(
                        modifier = Modifier.padding(horizontal = Dimensions.paddingMedium()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Nueva solicitud"
                        )
                        Spacer(Modifier.width(Dimensions.paddingSmall()))
                        Text(
                            "Nueva Solicitud",
                            fontWeight = FontWeight.Bold,
                            fontSize = Dimensions.fontSizeMedium()
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWhite)
                .padding(paddingValues)
        ) {
            // Header con ubicación y buscador
            Surface(
                shadowElevation = 4.dp,
                color = BackgroundWhite
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.paddingMedium())
                ) {
                    // Selector de ubicación
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showLocationDialog = true }
                            .padding(vertical = Dimensions.paddingSmall()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Ubicación",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(Dimensions.iconSizeMedium())
                        )

                        Spacer(Modifier.width(Dimensions.paddingSmall()))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ubicación del servicio",
                                fontSize = Dimensions.fontSizeSmall(),
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = currentLocation,
                                fontSize = Dimensions.fontSizeMedium(),
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Cambiar ubicación",
                            tint = TextSecondary,
                            modifier = Modifier.size(Dimensions.iconSizeMedium())
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = Dimensions.paddingMedium())
            ) {
                // Categorías
                item {
                    Column {
                        Text(
                            text = "Categorías",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = Dimensions.fontSizeLarge()
                            ),
                            color = TextPrimary,
                            modifier = Modifier.padding(horizontal = Dimensions.paddingMedium())
                        )

                        Spacer(Modifier.height(Dimensions.paddingSmall()))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall()),
                            contentPadding = PaddingValues(horizontal = Dimensions.paddingMedium())
                        ) {
                            items(categories) { category ->
                                CategoryCard(category)
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(Dimensions.paddingMedium())) }

                // Top Profesionales
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimensions.paddingMedium()),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Profesionales destacados",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = Dimensions.fontSizeLarge()
                            ),
                            color = TextPrimary
                        )

                        TextButton(onClick = { /* TODO: Ver todos */ }) {
                            Text(
                                "Ver todos",
                                color = PrimaryBlue,
                                fontSize = Dimensions.fontSizeSmall()
                            )
                        }
                    }

                    Spacer(Modifier.height(Dimensions.paddingSmall()))
                }

                items(topContractors) { contractor ->
                    ContractorCard(contractor)
                    Spacer(Modifier.height(Dimensions.paddingSmall()))
                }

                if (topContractors.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Dimensions.paddingLarge()),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay profesionales disponibles en tu área",
                                color = TextSecondary,
                                fontSize = Dimensions.fontSizeMedium()
                            )
                        }
                    }
                }
            }
        }
    }

    // Diálogo de ubicación
    if (showLocationDialog) {
        LocationSelectionDialog(
            currentLocation = currentLocation,
            locationManager = locationManager,
            onLocationSelected = { newLocation: String ->
                currentLocation = newLocation
                showLocationDialog = false
            },
            onDismiss = { showLocationDialog = false },
            onRequestPermission = {
                showLocationDialog = false
                shouldRequestPermission = true
            }
        )
    }
}

@Composable
fun LocationSelectionDialog(
    currentLocation: String,
    locationManager: LocationManager,
    onLocationSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    onRequestPermission: () -> Unit
) {
    var manualLocation by remember { mutableStateOf("") }
    var gpsLoading by remember { mutableStateOf(false) }
    var gpsError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val savedLocations = remember {
        listOf(
            "Guatemala City, Guatemala",
            "Antigua Guatemala, Sacatepéquez",
            "Quetzaltenango, Guatemala",
            "Escuintla, Guatemala"
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Ubicación del servicio",
                fontWeight = FontWeight.Bold,
                fontSize = Dimensions.fontSizeLarge()
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
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
                                            onLocationSelected(locationData.address)
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
                        containerColor = PrimaryBlue.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimensions.paddingMedium()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (gpsLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(Dimensions.iconSizeMedium()),
                                color = PrimaryBlue,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(Dimensions.iconSizeMedium())
                            )
                        }
                        Spacer(Modifier.width(Dimensions.paddingSmall()))
                        Column {
                            Text(
                                "Usar ubicación actual",
                                fontWeight = FontWeight.Bold,
                                fontSize = Dimensions.fontSizeMedium(),
                                color = TextPrimary
                            )
                            Text(
                                if (gpsLoading) "Obteniendo ubicación..." else "Detectar automáticamente",
                                fontSize = Dimensions.fontSizeSmall(),
                                color = TextSecondary
                            )
                        }
                    }
                }

                if (gpsError != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = gpsError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = Dimensions.fontSizeSmall(),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                Spacer(Modifier.height(Dimensions.paddingMedium()))

                Text(
                    "Ubicaciones guardadas",
                    fontSize = Dimensions.fontSizeSmall(),
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )

                Spacer(Modifier.height(Dimensions.paddingSmall()))

                savedLocations.forEach { location ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onLocationSelected(location) },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (location == currentLocation)
                                AccentYellow.copy(alpha = 0.15f)
                            else
                                BackgroundWhite
                        ),
                        border = if (location == currentLocation)
                            androidx.compose.foundation.BorderStroke(2.dp, AccentYellow)
                        else null
                    ) {
                        Text(
                            text = location,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Dimensions.paddingSmall()),
                            fontSize = Dimensions.fontSizeSmall(),
                            color = TextPrimary
                        )
                    }
                }

                Spacer(Modifier.height(Dimensions.paddingMedium()))

                OutlinedTextField(
                    value = manualLocation,
                    onValueChange = { manualLocation = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Escribe tu dirección",
                            fontSize = Dimensions.fontSizeSmall()
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = DividerGray
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = Dimensions.fontSizeMedium()
                    )
                )
            }
        },
        confirmButton = {
            if (manualLocation.isNotBlank()) {
                TextButton(
                    onClick = { onLocationSelected(manualLocation) }
                ) {
                    Text(
                        "Usar esta dirección",
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimensions.fontSizeSmall()
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Cancelar",
                    color = TextSecondary,
                    fontSize = Dimensions.fontSizeSmall()
                )
            }
        },
        containerColor = BackgroundWhite
    )
}

@Composable
fun CategoryCard(category: Category) {
    Card(
        modifier = Modifier
            .width(Dimensions.categoryCardWidth())
            .height(Dimensions.categoryCardHeight()),
        shape = RoundedCornerShape(Dimensions.paddingMedium()),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryBlue.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.paddingSmall()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = category.icon,
                fontSize = if (Dimensions.isCompact()) 28.sp else 32.sp
            )
            Spacer(Modifier.height(Dimensions.paddingSmall()))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = Dimensions.fontSizeSmall()
                ),
                color = TextPrimary,
                maxLines = 2,
                lineHeight = (Dimensions.fontSizeSmall().value + 2).sp
            )
        }
    }
}

@Composable
fun ContractorCard(contractor: Contractor) {
    val avatarSize = if (Dimensions.isCompact()) 50.dp else 60.dp
    val buttonMinWidth = if (Dimensions.isCompact()) 80.dp else 100.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimensions.paddingMedium()),
        shape = RoundedCornerShape(Dimensions.paddingMedium()),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimensions.cardElevation()
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.paddingMedium()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(avatarSize),
                shape = RoundedCornerShape(12.dp),
                color = AccentYellow.copy(alpha = 0.2f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = contractor.name.first().toString(),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = AccentYellow,
                        fontSize = if (Dimensions.isCompact()) 20.sp else 24.sp
                    )
                }
            }

            Spacer(Modifier.width(Dimensions.paddingMedium()))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = contractor.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = Dimensions.fontSizeMedium()
                        ),
                        color = TextPrimary
                    )

                    if (contractor.verified) {
                        Spacer(Modifier.width(4.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = PrimaryBlue.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "✓",
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                color = PrimaryBlue,
                                fontSize = Dimensions.fontSizeSmall(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Text(
                    text = contractor.profession,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    fontSize = Dimensions.fontSizeSmall()
                )

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AccentYellow,
                        modifier = Modifier.size(Dimensions.iconSizeSmall())
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${contractor.rating} • ${contractor.jobs} trabajos",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = Dimensions.fontSizeSmall()
                    )
                }
            }

            Button(
                onClick = { /* TODO: Contratar */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentYellow,
                    contentColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.widthIn(min = buttonMinWidth),
                contentPadding = PaddingValues(
                    horizontal = if (Dimensions.isCompact()) 12.dp else 16.dp,
                    vertical = 8.dp
                )
            ) {
                Text(
                    "Contratar",
                    fontSize = Dimensions.fontSizeSmall(),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
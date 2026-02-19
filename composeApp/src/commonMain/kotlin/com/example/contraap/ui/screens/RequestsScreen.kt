package com.example.contraap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.contraap.ui.theme.*
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.draw.rotate

data class Request(
    val id: String,
    val title: String,
    val contractor: String?,
    val contractorPhone: String?,
    val date: String,
    val status: RequestStatus,
    val price: String,
    val category: String,
    val location: String,
    val timeline: List<TimelineEvent>
)

data class TimelineEvent(
    val title: String,
    val description: String,
    val date: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean = false
)

enum class RequestStatus {
    PENDING,      // Pendiente (enviado)
    ACCEPTED,     // Aceptado
    IN_PROGRESS,  // En progreso
    COMPLETED,    // Completado
    CANCELLED     // Cancelado
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsScreen() {
    // Datos de ejemplo
    val requests = remember {
        listOf(
            Request(
                id = "#26-372417",
                title = "Reparación de plomería",
                contractor = "Juan Pérez",
                contractorPhone = "+502 5583 1667",
                date = "Hoy miércoles 18 de febrero",
                status = RequestStatus.IN_PROGRESS,
                price = "Q150.00",
                category = "Plomería",
                location = "11 AV 8 83, Zona 19 La Florida, Ciudad de Guatemala, GT",
                timeline = listOf(
                    TimelineEvent(
                        "Solicitud enviada",
                        "Tu solicitud ha sido enviada",
                        "17 de febrero, 10:30 AM",
                        isCompleted = true
                    ),
                    TimelineEvent(
                        "Aceptado",
                        "Juan Pérez aceptó tu solicitud",
                        "17 de febrero, 2:15 PM",
                        isCompleted = true
                    ),
                    TimelineEvent(
                        "En camino",
                        "El profesional está en camino",
                        "Hoy 18 de febrero, 9:00 AM",
                        isCompleted = true,
                        isCurrent = true
                    ),
                    TimelineEvent(
                        "Entrega estimada",
                        "hoy miércoles 18 de febrero",
                        "2:00 PM",
                        isCompleted = false
                    )
                )
            ),
            Request(
                id = "#26-372416",
                title = "Instalación eléctrica",
                contractor = "María García",
                contractorPhone = "+502 1234 5678",
                date = "15 de febrero",
                status = RequestStatus.COMPLETED,
                price = "Q300.00",
                category = "Electricista",
                location = "Zona 10, Ciudad de Guatemala",
                timeline = listOf(
                    TimelineEvent("Solicitud enviada", "15 de febrero", "10:00 AM", true),
                    TimelineEvent("Aceptado", "15 de febrero", "11:30 AM", true),
                    TimelineEvent("Completado", "15 de febrero", "4:00 PM", true, true)
                )
            ),
            Request(
                id = "#26-372415",
                title = "Pintura de casa",
                contractor = null,
                contractorPhone = null,
                date = "14 de febrero",
                status = RequestStatus.PENDING,
                price = "Q500.00",
                category = "Pintor",
                location = "Antigua Guatemala",
                timeline = listOf(
                    TimelineEvent(
                        "Solicitud enviada",
                        "Esperando respuesta de profesionales",
                        "14 de febrero, 3:00 PM",
                        isCompleted = true,
                        isCurrent = true
                    )
                )
            )
        )
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Todas", "Pendientes", "En Progreso", "Completadas")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        // Header
        Surface(
            shadowElevation = 4.dp,
            color = BackgroundWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Solicitudes",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        ),
                        color = TextPrimary
                    )

                    IconButton(onClick = { /* Filtros */ }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtros",
                            tint = PrimaryBlue
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = BackgroundWhite,
                    contentColor = PrimaryBlue,
                    indicator = { tabPositions ->
                        if (selectedTab < tabPositions.size) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize(Alignment.BottomStart)
                                    .offset(x = tabPositions[selectedTab].left)
                                    .width(tabPositions[selectedTab].width)
                                    .height(3.dp)
                                    .background(AccentYellow)
                            )
                        }
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            },
                            selectedContentColor = PrimaryBlue,
                            unselectedContentColor = TextSecondary
                        )
                    }
                }
            }
        }

        // Lista de solicitudes
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(requests) { request ->
                RequestDetailCard(request)
            }
        }
    }
}

@Composable
fun RequestDetailCard(request: Request) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        onClick = { isExpanded = !isExpanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header del pedido
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido ${request.id}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = TextPrimary
                )

                StatusBadge(status = request.status)
            }

            Spacer(Modifier.height(12.dp))

            // Fecha de recepción
            Text(
                text = "Recibe ${request.date}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = TextPrimary
            )

            Spacer(Modifier.height(16.dp))

            // Timeline de estados
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                request.timeline.forEachIndexed { index, event ->
                    TimelineItem(
                        event = event,
                        isLast = index == request.timeline.lastIndex
                    )
                }
            }

            if (isExpanded) {
                Spacer(Modifier.height(16.dp))

                Divider(color = DividerGray, thickness = 1.dp)

                Spacer(Modifier.height(16.dp))

                // Detalles del servicio
                Text(
                    text = "Servicio",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = TextPrimary
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = request.title,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = request.category,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Text(
                        text = request.price,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = PrimaryBlue
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Total del pedido
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total del pedido",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = TextPrimary
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Text(
                            text = "Pagado",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Divider(color = DividerGray, thickness = 1.dp)

                Spacer(Modifier.height(16.dp))

                // Información del profesional
                if (request.contractor != null) {
                    Text(
                        text = "Profesional",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = TextPrimary
                    )

                    Spacer(Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF5F5F5)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = request.contractor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextPrimary
                            )
                            if (request.contractorPhone != null) {
                                Text(
                                    text = request.contractorPhone,
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = request.location,
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Indicador de expandir/contraer
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = if (isExpanded) "Contraer" else "Expandir",
                    tint = TextSecondary,
                    modifier = Modifier
                        .size(24.dp)
                        .then(
                            if (isExpanded)
                                Modifier.rotate(270f)
                            else
                                Modifier.rotate(90f)
                        )
                )
            }
        }
    }
}

@Composable
fun TimelineItem(
    event: TimelineEvent,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Círculo e indicador
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = when {
                    event.isCurrent -> AccentYellow
                    event.isCompleted -> Color(0xFF4CAF50)
                    else -> Color(0xFFE0E0E0)
                }
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    if (event.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Surface(
                            modifier = Modifier.size(12.dp),
                            shape = CircleShape,
                            color = Color.White
                        ) {}
                    }
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(
                            if (event.isCompleted) Color(0xFF4CAF50) else Color(0xFFE0E0E0)
                        )
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        // Contenido del evento
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (!isLast) 16.dp else 0.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = if (event.isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize = 15.sp
                ),
                color = if (event.isCurrent) TextPrimary else TextSecondary
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = event.description,
                fontSize = 13.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun StatusBadge(status: RequestStatus) {
    val (color, text, bgColor) = when (status) {
        RequestStatus.PENDING -> Triple(
            Color(0xFFFFC107),
            "Pendiente",
            Color(0xFFFFF9E6)
        )
        RequestStatus.ACCEPTED -> Triple(
            PrimaryBlue,
            "Aceptado",
            PrimaryBlue.copy(alpha = 0.15f)
        )
        RequestStatus.IN_PROGRESS -> Triple(
            AccentYellow,
            "En Progreso",
            AccentYellow.copy(alpha = 0.15f)
        )
        RequestStatus.COMPLETED -> Triple(
            Color(0xFF4CAF50),
            "Completado",
            Color(0xFFE8F5E9)
        )
        RequestStatus.CANCELLED -> Triple(
            Color(0xFFE53935),
            "Cancelado",
            Color(0xFFFFEBEE)
        )
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = bgColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = color,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        )
    }
}
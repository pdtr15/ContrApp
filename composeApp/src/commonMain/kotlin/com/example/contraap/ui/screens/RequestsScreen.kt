package com.example.contraap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contraap.ui.theme.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

data class Request(
    val id: String,
    val title: String,
    val contractor: String,
    val date: String,
    val status: RequestStatus,
    val price: String
)

enum class RequestStatus {
    PENDING, ACCEPTED, COMPLETED, CANCELLED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsScreen() {
    // Datos de ejemplo
    val requests = remember {
        listOf(
            Request("1", "Reparación de plomería", "Juan Pérez", "15 Ene 2024", RequestStatus.PENDING, "$150"),
            Request("2", "Instalación eléctrica", "María García", "12 Ene 2024", RequestStatus.ACCEPTED, "$300"),
            Request("3", "Pintura de casa", "Carlos López", "10 Ene 2024", RequestStatus.COMPLETED, "$500"),
            Request("4", "Reparación de techo", "Ana Martínez", "8 Ene 2024", RequestStatus.CANCELLED, "$250")
        )
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Todas", "Pendientes", "Aceptadas", "Completadas")

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
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = BackgroundWhite,
                    contentColor = PrimaryBlue,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        if (selectedTab < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = AccentYellow,
                                height = 3.dp
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(requests) { request ->
                RequestCard(request)
            }
        }
    }
}

@Composable
fun RequestCard(request: Request) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
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
                    text = request.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )

                StatusChip(status = request.status)
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = request.contractor,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = request.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary.copy(alpha = 0.7f),
                        fontSize = 12.sp
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
        }
    }
}

@Composable
fun StatusChip(status: RequestStatus) {
    val (color, text) = when (status) {
        RequestStatus.PENDING -> Pair(AccentYellow, "Pendiente")
        RequestStatus.ACCEPTED -> Pair(PrimaryBlue, "Aceptada")
        RequestStatus.COMPLETED -> Pair(androidx.compose.ui.graphics.Color(0xFF4CAF50), "Completada")
        RequestStatus.CANCELLED -> Pair(androidx.compose.ui.graphics.Color(0xFFE53935), "Cancelada")
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.15f)
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
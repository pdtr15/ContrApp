package com.example.contraap.ui.screens.contractor

import VisitCostDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// --- COLORES BASADOS EN TU DISEÑO ---
private val LightBackground = Color(0xFFF5F7FA)
private val CardBlue = Color(0xFF4FC3F7)
private val ButtonYellow = Color(0xFFFFCA28)
private val TextDark = Color(0xFF2D3142)
private val TextGray = Color(0xFF9098B1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractorDashboardScreen(navController: NavController) {
    var isOnline by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(0) }

    // ESTADO PARA MOSTRAR/OCULTAR EL MODAL DE VISITA
    var showScheduleModal by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = LightBackground,
        topBar = { DashboardTopBar() },
        bottomBar = { DashboardBottomBar(selectedTab) { selectedTab = it } }
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

            // 1. Tarjetas de Finanzas (Ingresos y Balance)
            FinancialCardsRow()

            // 2. Estado del Servicio (Switch En línea / Desconectado)
            ServiceStatusCard(isOnline = isOnline, onStatusChange = { isOnline = it })

            // 3. Nuevas Solicitudes (Tarjeta con temporizador y botón Visita)
            NewRequestsSection(
                navController = navController,
                onScheduleVisitClick = { showScheduleModal = true } // ABRE EL MODAL AQUÍ
            )

            // 4. Próxima Agenda
            UpcomingAgendaSection()

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showScheduleModal) {
        VisitCostDialog(
            onDismiss = { showScheduleModal = false },
            onConfirm = { monto ->
                // TODO: Guardar el monto y notificar al cliente desde el Dashboard
                println("Visita agendada desde el dashboard por: $monto")
                showScheduleModal = false
            }
        )
    }
}

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
                    Icon(Icons.Default.Build, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("CONTRAAPP", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
            }
        },
        actions = {
            IconButton(onClick = { /* Abrir notificaciones */ }) {
                BadgedBox(
                    badge = { Badge(containerColor = Color.Red) { Text("1") } }
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = TextDark)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                        color = if (isOnline) CardBlue else TextGray
                    )
                }
            }
            Switch(
                checked = isOnline,
                onCheckedChange = onStatusChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = CardBlue
                )
            )
        }
    }
}

@Composable
fun NewRequestsSection(
    navController: NavController,
    onScheduleVisitClick: () -> Unit // Recibe la orden de abrir el modal
) {
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
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("1 Nueva", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CardBlue)
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(50.dp)) {
                        CircularProgressIndicator(
                            progress = { 0.7f },
                            modifier = Modifier.fillMaxSize(),
                            color = ButtonYellow,
                            strokeWidth = 3.dp,
                            trackColor = LightBackground
                        )
                        Text("22s", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { navController.navigate("job_detail") }
                            .padding(4.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Fuga de Plomería", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                            Text("$85.00", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CardBlue)
                        }
                        Text("Alice Smith • A 3.8 km", fontSize = 12.sp, color = TextGray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // BOTONES ACTUALIZADOS: 3 Botones alineados
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { /* Rechazar trabajo */ },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Rechazar", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onScheduleVisitClick, // ABRE EL MODAL DE VISITA
                        modifier = Modifier.weight(1f).height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonYellow),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Visita", color = TextDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { /* Aceptar trabajo */ },
                        modifier = Modifier.weight(1f).height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CardBlue),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Aceptar", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
fun AgendaItemCard(date: String, time: String, title: String, statusText: String, statusColor: Color) {
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
                Text(date, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
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

@Composable
fun DashboardBottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Panel") },
            label = { Text("PANEL", fontSize = 10.sp) },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = CardBlue, selectedTextColor = CardBlue, indicatorColor = CardBlue.copy(alpha = 0.1f))
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Work, contentDescription = "Trabajos") },
            label = { Text("TRABAJOS", fontSize = 10.sp) },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = CardBlue, selectedTextColor = CardBlue, indicatorColor = CardBlue.copy(alpha = 0.1f))
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AttachMoney, contentDescription = "Ganancias") },
            label = { Text("GANANCIAS", fontSize = 10.sp) },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = CardBlue, selectedTextColor = CardBlue, indicatorColor = CardBlue.copy(alpha = 0.1f))
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("PERFIL", fontSize = 10.sp) },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = CardBlue, selectedTextColor = CardBlue, indicatorColor = CardBlue.copy(alpha = 0.1f))
        )
    }
}

package com.example.contraap.ui.screens.contractor

import VisitCostDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Reutilizamos los colores
private val LightBackground = Color(0xFFF5F7FA)
private val CardBlue = Color(0xFF4FC3F7)
private val ButtonYellow = Color(0xFFFFCA28)
private val TextDark = Color(0xFF2D3142)
private val TextGray = Color(0xFF9098B1)
private val DangerRed = Color(0xFFFF5252)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobRequestDetailScreen(
    onBackClick: () -> Unit = {}
) {
    // Estado para mostrar el modal de la visita
    var showVisitDialog by remember { mutableStateOf(false) }
    var visitAmount by remember { mutableStateOf("") }

    Scaffold(
        containerColor = LightBackground,
        topBar = {
            TopAppBar(
                title = { Text("Nueva Solicitud", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground)
            )
        },
        bottomBar = {
            BottomActionSection(
                onReject = { /* Lógica rechazar */ },
                onAccept = { /* Lógica aceptar */ },
                onRequestVisit = { showVisitDialog = true }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- ENCABEZADO: Título y Precio ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text("Plomería", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("$450 - $600 MXN", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CardBlue)
                        Text(" (Estimado)", fontSize = 14.sp, color = TextGray)
                    }
                }

                // Etiqueta de tiempo (Timer rojo)
                Row(
                    modifier = Modifier
                        .background(DangerRed.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = DangerRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("14:32", color = DangerRed, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            // --- TARJETA DEL CLIENTE ---
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar falso
                    Box(
                        modifier = Modifier.size(50.dp).background(LightBackground, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = TextGray)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Maria Rodriguez", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = ButtonYellow, modifier = Modifier.size(14.dp))
                            Text(" 4.8 (120 reseñas) • ", fontSize = 12.sp, color = TextGray)
                            Text("Cliente Frecuente", fontSize = 12.sp, color = CardBlue, fontWeight = FontWeight.Medium)
                        }
                    }
                    IconButton(onClick = { /* Abrir chat */ }) {
                        Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Chat", tint = TextGray)
                    }
                }
            }

            // --- TARJETA DE UBICACIÓN ---
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("UBICACIÓN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Text("A 4.5 km", fontSize = 12.sp, color = TextGray)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    // Placeholder del Mapa
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LightBackground)
                            .border(1.dp, DividerDefaults.color, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = CardBlue, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Dirección aproximada", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Colonia Roma Norte, CDMX", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                            Text("Calle Orizaba 123, Int 4", fontSize = 12.sp, color = TextGray)
                        }
                    }
                }
            }

            // --- TARJETA DETALLES DEL TRABAJO ---
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("DETALLES DEL TRABAJO", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Necesito arreglar una fuga en el lavabo del baño principal. Está goteando constantemente y me preocupa que dañe el mueble de madera. Es algo urgente, por favor traer herramientas para tubería de PVC.",
                        fontSize = 14.sp,
                        color = TextDark,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // --- MODAL: SOLICITAR VISITA ---
    if (showVisitDialog) {
        VisitCostDialog(
            onDismiss = { showVisitDialog = false },
            onConfirm = { monto ->
                // TODO: Lógica para enviar el monto de la visita
                println("Se enviará propuesta por: $monto")
                showVisitDialog = false
            }
        )
    }
}
@Composable
fun BottomActionSection(
    onReject: () -> Unit,
    onAccept: () -> Unit,
    onRequestVisit: () -> Unit
) {
    Surface(
        shadowElevation = 16.dp,
        color = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding() // <--- ESTA ES LA SOLUCIÓN MAGICA
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Fila superior: Rechazar y Visita
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextDark)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Rechazar")
                }

                Button(
                    onClick = onRequestVisit,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonYellow, contentColor = TextDark)
                ) {
                    Icon(Icons.Default.DirectionsCar, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Visita", fontWeight = FontWeight.Bold)
                }
            }

            // Fila inferior: Aceptar (Botón principal)
            Button(
                onClick = onAccept,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CardBlue)
            ) {
                Text("ACEPTAR TRABAJO", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
            }
        }
    }
}
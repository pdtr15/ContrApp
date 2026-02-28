package com.example.contraap.ui.screens.admin

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*

// ── Paleta de colores de la app ───────────────────────────────────────────────
private val PrimaryBlue   = Color(0xFF4EB0E3)
private val AccentYellow  = Color(0xFFFDC815)
private val BgWhite       = Color(0xFFFFFFFF)
private val TextPrimary   = Color(0xFF1E1E1E)
private val TextSecondary = Color(0xFF6E6E6E)
private val DividerGray   = Color(0xFFE0E0E0)
private val GreenApprove  = Color(0xFF2ECC71)
private val RedReject     = Color(0xFFE74C3C)
private val BgLight       = Color(0xFFF5F8FB)

// ── Modelos de datos ──────────────────────────────────────────────────────────
enum class ContractorStatus { PENDING, APPROVED, REJECTED }

data class ContractorRequest(
    val id: Int,
    val name: String,
    val specialty: String,
    val joinedDate: String,
    val phone: String,
    val email: String,
    val experience: String,
    val status: ContractorStatus,
    val hasIdDocument: Boolean = true,
    val hasCertificate: Boolean = true,
    val rejectionReason: String = ""
)

// ── Pantalla principal ────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminValidationScreen(
    onBack: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedContractor by remember { mutableStateOf<ContractorRequest?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val contractors = remember {
        mutableStateListOf(
            ContractorRequest(1, "Carlos Rodríguez", "Electricista", "Oct 12, 2023", "+502 5555-1234", "carlos@email.com", "5 años", ContractorStatus.PENDING),
            ContractorRequest(2, "María González", "Plomería & Calefacción", "Oct 14, 2023", "+502 5555-5678", "maria@email.com", "8 años", ContractorStatus.PENDING),
            ContractorRequest(3, "Juan Pérez", "Contratista General", "Oct 15, 2023", "+502 5555-9012", "juan@email.com", "12 años", ContractorStatus.PENDING)
        )
    }

    val tabs = listOf(
        "Pendientes" to contractors.count { it.status == ContractorStatus.PENDING },
        "Aprobados"  to contractors.count { it.status == ContractorStatus.APPROVED },
        "Rechazados" to contractors.count { it.status == ContractorStatus.REJECTED }
    )

    val filtered = contractors.filter { c ->
        val matchTab = when (selectedTab) {
            0 -> c.status == ContractorStatus.PENDING
            1 -> c.status == ContractorStatus.APPROVED
            else -> c.status == ContractorStatus.REJECTED
        }
        val matchSearch = searchQuery.isBlank() ||
                c.name.contains(searchQuery, ignoreCase = true) ||
                c.specialty.contains(searchQuery, ignoreCase = true)
        matchTab && matchSearch
    }

    Scaffold(
        containerColor = BgLight,
        topBar = {
            AdminTopBar(
                pendingCount = contractors.count { it.status == ContractorStatus.PENDING },
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Buscador
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )

            // Tabs
            AdminTabs(
                tabs = tabs,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            // Lista
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (filtered.isEmpty()) {
                    item { EmptyState(selectedTab) }
                } else {
                    items(filtered, key = { it.id }) { contractor ->
                        ContractorCard(
                            contractor = contractor,
                            isExpanded = selectedContractor?.id == contractor.id,
                            onClick = {
                                selectedContractor =
                                    if (selectedContractor?.id == contractor.id) null
                                    else contractor
                            },
                            onApprove = {
                                val idx = contractors.indexOfFirst { it.id == contractor.id }
                                if (idx >= 0) contractors[idx] = contractors[idx].copy(status = ContractorStatus.APPROVED)
                                selectedContractor = null
                            },
                            onReject = { reason ->
                                val idx = contractors.indexOfFirst { it.id == contractor.id }
                                if (idx >= 0) contractors[idx] = contractors[idx].copy(
                                    status = ContractorStatus.REJECTED,
                                    rejectionReason = reason
                                )
                                selectedContractor = null
                            }
                        )
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

// ── TopBar ────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminTopBar(pendingCount: Int, onBack: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = TextPrimary)
            }
        },
        title = {
            Column {
                Text(
                    "Validaciones",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )
                Text(
                    "ContrApp Admin",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        actions = {
            if (pendingCount > 0) {
                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .background(AccentYellow, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        "$pendingCount Pendientes",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BgLight)
    )
}

// ── Buscador ──────────────────────────────────────────────────────────────────
@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = {
            Text("Buscar por nombre o especialidad", fontSize = 14.sp, color = TextSecondary)
        },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryBlue)
        },
        trailingIcon = {
            if (query.isNotBlank()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                }
            }
        },
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryBlue,
            unfocusedBorderColor = DividerGray,
            focusedContainerColor = BgWhite,
            unfocusedContainerColor = BgWhite
        ),
        singleLine = true
    )
}

// ── Tabs ──────────────────────────────────────────────────────────────────────
@Composable
private fun AdminTabs(
    tabs: List<Pair<String, Int>>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(BgWhite, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        tabs.forEachIndexed { index, (label, count) ->
            val selected = selectedTab == index
            val tabColor = when (index) {
                0 -> AccentYellow
                1 -> GreenApprove
                else -> RedReject
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (selected) tabColor.copy(alpha = 0.15f) else Color.Transparent,
                        RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = if (selected) 1.5.dp else 0.dp,
                        color = if (selected) tabColor else Color.Transparent,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        label,
                        fontSize = 12.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        color = if (selected) tabColor else TextSecondary
                    )
                    if (count > 0) {
                        Text(
                            "$count",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) tabColor else TextSecondary.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
    Spacer(Modifier.height(8.dp))
}

// ── Card de contratista ───────────────────────────────────────────────────────
@Composable
private fun ContractorCard(
    contractor: ContractorRequest,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onApprove: () -> Unit,
    onReject: (String) -> Unit
) {
    var rejectionReason by remember { mutableStateOf("") }
    var showRejectField by remember(isExpanded) { mutableStateOf(false) }

    val statusColor = when (contractor.status) {
        ContractorStatus.PENDING  -> AccentYellow
        ContractorStatus.APPROVED -> GreenApprove
        ContractorStatus.REJECTED -> RedReject
    }
    val statusLabel = when (contractor.status) {
        ContractorStatus.PENDING  -> "Pendiente"
        ContractorStatus.APPROVED -> "Aprobado"
        ContractorStatus.REJECTED -> "Rechazado"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BgWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isExpanded) 6.dp else 2.dp)
    ) {
        Column {
            // Fila principal — siempre visible
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(PrimaryBlue.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        contractor.name.first().toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = PrimaryBlue
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        contractor.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Text(
                        contractor.specialty,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Text(
                        "Registrado: ${contractor.joinedDate}",
                        fontSize = 11.sp,
                        color = TextSecondary.copy(alpha = 0.7f)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    // Badge de estado
                    Box(
                        modifier = Modifier
                            .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            statusLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    // Indicador expandir
                    if (contractor.status == ContractorStatus.PENDING) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Panel expandido — solo para pendientes (o ver detalle de aprobados/rechazados)
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BgLight)
                        .padding(16.dp)
                ) {
                    HorizontalDivider(color = DividerGray, modifier = Modifier.padding(bottom = 12.dp))

                    // Datos personales
                    Text(
                        "Datos del Contratista",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(8.dp))

                    InfoRow(Icons.Default.Phone, "Teléfono", contractor.phone)
                    InfoRow(Icons.Default.Email, "Correo", contractor.email)
                    InfoRow(Icons.Default.WorkHistory, "Experiencia", contractor.experience)

                    Spacer(Modifier.height(12.dp))

                    // Documentos
                    Text(
                        "Documentos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        DocumentCard(
                            modifier = Modifier.weight(1f),
                            label = "DPI / Identificación",
                            available = contractor.hasIdDocument
                        )
                        DocumentCard(
                            modifier = Modifier.weight(1f),
                            label = "Certificado RTU",
                            available = contractor.hasCertificate
                        )
                    }

                    // Razón de rechazo si ya fue rechazado
                    if (contractor.status == ContractorStatus.REJECTED && contractor.rejectionReason.isNotBlank()) {
                        Spacer(Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(RedReject.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = RedReject, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "Razón: ${contractor.rejectionReason}",
                                    fontSize = 12.sp,
                                    color = RedReject,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Botones de acción solo para pendientes
                    if (contractor.status == ContractorStatus.PENDING) {
                        Spacer(Modifier.height(12.dp))

                        // Campo razón de rechazo
                        AnimatedVisibility(visible = showRejectField) {
                            Column {
                                Text(
                                    "RAZÓN DE RECHAZO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary,
                                    letterSpacing = 0.8.sp
                                )
                                Spacer(Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = rejectionReason,
                                    onValueChange = { rejectionReason = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = {
                                        Text(
                                            "Ej: Documento vencido o imagen borrosa...",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = RedReject,
                                        unfocusedBorderColor = DividerGray,
                                        focusedContainerColor = BgWhite,
                                        unfocusedContainerColor = BgWhite
                                    ),
                                    minLines = 2,
                                    maxLines = 3
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Botón Rechazar
                            Button(
                                onClick = {
                                    if (showRejectField && rejectionReason.isNotBlank()) {
                                        onReject(rejectionReason)
                                    } else {
                                        showRejectField = !showRejectField
                                    }
                                },
                                modifier = Modifier.weight(1f).height(46.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (showRejectField) RedReject else RedReject.copy(alpha = 0.1f),
                                    contentColor = if (showRejectField) BgWhite else RedReject
                                ),
                                shape = RoundedCornerShape(12.dp),
                                elevation = ButtonDefaults.buttonElevation(0.dp)
                            ) {
                                Icon(
                                    Icons.Default.Cancel,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    if (showRejectField && rejectionReason.isNotBlank()) "Confirmar" else "Rechazar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            // Botón Aprobar
                            Button(
                                onClick = onApprove,
                                modifier = Modifier.weight(1f).height(46.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryBlue,
                                    contentColor = BgWhite
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "Aprobar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Fila de info ──────────────────────────────────────────────────────────────
@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(15.dp))
        Spacer(Modifier.width(8.dp))
        Text("$label: ", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
        Text(value, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}

// ── Card de documento ─────────────────────────────────────────────────────────
@Composable
private fun DocumentCard(modifier: Modifier, label: String, available: Boolean) {
    Box(
        modifier = modifier
            .height(80.dp)
            .background(
                if (available) PrimaryBlue.copy(alpha = 0.07f) else RedReject.copy(alpha = 0.07f),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                if (available) PrimaryBlue.copy(alpha = 0.3f) else RedReject.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = if (available) Icons.Default.Description else Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = if (available) PrimaryBlue else RedReject,
                modifier = Modifier.size(26.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (available) PrimaryBlue else RedReject,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
            Text(
                if (available) "✓ Disponible" else "✗ Faltante",
                fontSize = 9.sp,
                color = if (available) GreenApprove else RedReject,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ── Estado vacío ──────────────────────────────────────────────────────────────
@Composable
private fun EmptyState(tab: Int) {
    val (icon, message) = when (tab) {
        0 -> Icons.Default.HourglassEmpty to "No hay solicitudes pendientes"
        1 -> Icons.Default.CheckCircleOutline to "Ningún contratista aprobado aún"
        else -> Icons.Default.Cancel to "Sin contratistas rechazados"
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = DividerGray, modifier = Modifier.size(56.dp))
            Spacer(Modifier.height(12.dp))
            Text(message, fontSize = 15.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
        }
    }
}
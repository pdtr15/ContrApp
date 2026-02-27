package com.example.contraap.ui.screens.contractor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

private val LightBackground = Color(0xFFF5F7FA)
private val CardBlue = Color(0xFF4FC3F7)
private val ButtonYellow = Color(0xFFFFCA28)
private val TextDark = Color(0xFF2D3142)
private val TextGray = Color(0xFF9098B1)
private val GreenSuccess = Color(0xFF4CAF50)

data class Transaction(
    val id: String,
    val title: String,
    val date: String,
    val amount: String,
    val type: TransactionType
)

enum class TransactionType {
    INCOME,    // Ingreso
    WITHDRAWAL // Retiro
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarningsScreen() {
    var selectedPeriod by remember { mutableStateOf(0) }
    val periods = listOf("Hoy", "Semana", "Mes", "Año")

    val transactions = remember {
        listOf(
            Transaction("1", "Reparación de Plomería", "Hoy 02:30 PM", "+$85.00", TransactionType.INCOME),
            Transaction("2", "Instalación Eléctrica", "Ayer 10:15 AM", "+$150.00", TransactionType.INCOME),
            Transaction("3", "Retiro a cuenta bancaria", "20 Oct", "-$500.00", TransactionType.WITHDRAWAL),
            Transaction("4", "Pintura de Casa", "19 Oct", "+$300.00", TransactionType.INCOME),
            Transaction("5", "Jardinería", "18 Oct", "+$75.00", TransactionType.INCOME)
        )
    }

    Scaffold(
        containerColor = LightBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Ganancias",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextDark
                    )
                },
                actions = {
                    IconButton(onClick = { /* Estadísticas */ }) {
                        Icon(Icons.Default.BarChart, contentDescription = "Estadísticas", tint = CardBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)) }

            // Cards de resumen
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBlue),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                            Spacer(Modifier.height(12.dp))
                            Text("BALANCE TOTAL", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                            Text("$1,240.50", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Stats cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        icon = Icons.Default.TrendingUp,
                        title = "Este Mes",
                        value = "$2,450.00",
                        change = "+12.5%",
                        isPositive = true,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = Icons.Default.Work,
                        title = "Trabajos",
                        value = "18",
                        change = "+3",
                        isPositive = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Filtro por período
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        periods.forEachIndexed { index, period ->
                            FilterChip(
                                selected = selectedPeriod == index,
                                onClick = { selectedPeriod = index },
                                label = { Text(period, fontSize = 13.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CardBlue,
                                    selectedLabelColor = Color.White,
                                    containerColor = LightBackground
                                )
                            )
                        }
                    }
                }
            }

            // Historial de transacciones
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Historial de Transacciones", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Button(
                        onClick = { /* Retirar */ },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.AccountBalance, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Retirar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(transactions) { transaction ->
                TransactionCard(transaction)
            }

            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    change: String,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = CardBlue, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(8.dp))
            Text(title, fontSize = 11.sp, color = TextGray, fontWeight = FontWeight.Medium)
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (isPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint = if (isPositive) GreenSuccess else Color.Red,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(change, fontSize = 11.sp, color = if (isPositive) GreenSuccess else Color.Red, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TransactionCard(transaction: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (transaction.type == TransactionType.INCOME)
                    GreenSuccess.copy(alpha = 0.1f)
                else
                    Color(0xFFFF5252).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        if (transaction.type == TransactionType.INCOME) Icons.Default.Add else Icons.Default.Remove,
                        contentDescription = null,
                        tint = if (transaction.type == TransactionType.INCOME) GreenSuccess else Color(0xFFFF5252),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(transaction.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Text(transaction.date, fontSize = 12.sp, color = TextGray)
            }

            Text(
                transaction.amount,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (transaction.type == TransactionType.INCOME) GreenSuccess else Color(0xFFFF5252)
            )
        }
    }
}
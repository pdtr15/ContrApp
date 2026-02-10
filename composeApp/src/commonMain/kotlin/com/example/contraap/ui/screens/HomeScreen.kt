package com.example.contraap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contraap.ui.theme.*

data class Category(val name: String, val icon: String)
data class Contractor(val name: String, val profession: String, val rating: Double, val jobs: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val categories = remember {
        listOf(
            Category("Plomería", "🔧"),
            Category("Electricidad", "⚡"),
            Category("Pintura", "🎨"),
            Category("Carpintería", "🪚"),
            Category("Limpieza", "🧹")
        )
    }

    val topContractors = remember {
        listOf(
            Contractor("Juan Pérez", "Plomero", 4.8, 127),
            Contractor("María García", "Electricista", 4.9, 203),
            Contractor("Carlos López", "Pintor", 4.7, 156)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        // Header con buscador
        Surface(
            shadowElevation = 4.dp,
            color = BackgroundWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Encuentra tu profesional",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = TextPrimary
                )

                Spacer(Modifier.height(16.dp))

                // Barra de búsqueda
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar servicios...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = TextSecondary
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = DividerGray,
                        focusedContainerColor = BackgroundWhite,
                        unfocusedContainerColor = BackgroundWhite
                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Categorías
            item {
                Column {
                    Text(
                        text = "Categorías",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(categories) { category ->
                            CategoryCard(category)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }

            // Top Profesionales
            item {
                Text(
                    text = "Profesionales destacados",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(12.dp))
            }

            items(topContractors) { contractor ->
                ContractorCard(contractor)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun CategoryCard(category: Category) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryBlue.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = category.icon,
                fontSize = 32.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                ),
                color = TextPrimary
            )
        }
    }
}

@Composable
fun ContractorCard(contractor: Contractor) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Surface(
                modifier = Modifier.size(60.dp),
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
                        color = AccentYellow
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contractor.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = TextPrimary
                )
                Text(
                    text = contractor.profession,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AccentYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${contractor.rating} • ${contractor.jobs} trabajos",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentYellow,
                    contentColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Contratar", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
package com.example.contraap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contraap.ui.theme.*

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        // Header con perfil
        Surface(
            shadowElevation = 4.dp,
            color = PrimaryBlue,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = BackgroundWhite
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "U",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = PrimaryBlue
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Usuario Demo",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = BackgroundWhite
                )

                Text(
                    text = "usuario@ejemplo.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BackgroundWhite.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SettingsSection(title = "Cuenta") {
                    SettingsItem(icon = Icons.Default.Person, title = "Editar perfil")
                    SettingsItem(icon = Icons.Default.Lock, title = "Cambiar contraseña")
                    SettingsItem(icon = Icons.Default.Notifications, title = "Notificaciones")
                }
            }

            item {
                SettingsSection(title = "Preferencias") {
                    SettingsItem(icon = Icons.Default.Language, title = "Idioma")
                    SettingsItem(icon = Icons.Default.DarkMode, title = "Tema")
                }
            }

            item {
                SettingsSection(title = "Soporte") {
                    SettingsItem(icon = Icons.Default.Help, title = "Ayuda")
                    SettingsItem(icon = Icons.Default.Info, title = "Acerca de")
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFFE53935),
                        contentColor = BackgroundWhite
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar sesión", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            color = TextSecondary,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = BackgroundWhite
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 15.sp
            ),
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextSecondary
        )
    }

    Divider(color = DividerGray, thickness = 0.5.dp)
}
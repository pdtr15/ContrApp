package com.example.contraap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contraap.ui.theme.*

data class ChatMessage(
    val name: String,
    val lastMessage: String,
    val time: String,
    val unread: Int
)

@Composable
fun MessagesScreen() {
    val messages = remember {
        listOf(
            ChatMessage("Juan Pérez", "¿A qué hora puedes venir?", "10:30 AM", 2),
            ChatMessage("María García", "Perfecto, nos vemos mañana", "Ayer", 0),
            ChatMessage("Carlos López", "Gracias por tu trabajo", "Hace 2 días", 0),
            ChatMessage("Ana Martínez", "¿Cuánto cobraría por...?", "Hace 3 días", 1)
        )
    }

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
            Text(
                text = "Mensajes",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = TextPrimary,
                modifier = Modifier.padding(16.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                MessageCard(message)
            }
        }
    }
}

@Composable
fun MessageCard(message: ChatMessage) {
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = PrimaryBlue.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = message.name.first().toString(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = PrimaryBlue
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = message.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = TextPrimary
                    )
                    Text(
                        text = message.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.lastMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )

                    if (message.unread > 0) {
                        Surface(
                            modifier = Modifier.size(24.dp),
                            shape = CircleShape,
                            color = AccentYellow
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = message.unread.toString(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = TextPrimary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.example.contraap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.contraap.ui.theme.*

data class Message(
    val id: String,
    val text: String,
    val time: String,
    val isSentByMe: Boolean,
    val imageUrl: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    chat: ChatMessage,
    onBackClick: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Mensajes de ejemplo
    val messages = remember {
        mutableStateListOf(
            Message(
                "1",
                "\n" +
                        "¡Hola! Ya llegué a la propiedad para la inspección.",
                "10:16 AM",
                false
            ),
            Message(
                "2",
                "¡Genial! ¿Podrías enviarme un presupuesto?",
                "10:17 AM",
                true
            ),
            Message(
                "3",
                "\n" +
                        "Claro, subiré el presupuesto ahora. Aquí está la foto de lo que recomiendo para el servicio.",
                "10:20 AM",
                false,
                imageUrl = "placeholder"
            ),
            Message(
                "4",
                "¡Se ven perfecto!",
                "10:22 AM",
                true
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = PrimaryBlue.copy(alpha = 0.2f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = chat.name.first().toString(),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = PrimaryBlue
                                )
                            }
                        }

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = chat.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = Dimensions.fontSizeMedium()
                                    )
                                )
                                if (chat.isOnline) {
                                    Spacer(Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Online",
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            Text(
                                text = if (chat.isOnline) "Online" else "Offline",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = Dimensions.fontSizeSmall(),
                                color = if (chat.isOnline) Color(0xFF4CAF50) else TextSecondary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Llamar */ }) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Llamar",
                            tint = PrimaryBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundWhite
                )
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = BackgroundWhite
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.paddingMedium()),
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Botón de cámara
                    IconButton(
                        onClick = { /* TODO: Abrir cámara */ },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adjuntar",
                            tint = TextSecondary,
                            modifier = Modifier.size(Dimensions.iconSizeMedium())
                        )
                    }

                    Spacer(Modifier.width(8.dp))

                    // Campo de texto
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                "Type a message...",
                                fontSize = Dimensions.fontSizeSmall(),
                                color = TextSecondary.copy(alpha = 0.5f)
                            )
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DividerGray,
                            unfocusedBorderColor = DividerGray,
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5)
                        ),
                        maxLines = 4,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = Dimensions.fontSizeMedium()
                        )
                    )

                    Spacer(Modifier.width(8.dp))

                    // Botón enviar
                    FloatingActionButton(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                messages.add(
                                    Message(
                                        (messages.size + 1).toString(),
                                        messageText,
                                        "Now",
                                        true
                                    )
                                )
                                messageText = ""
                            }
                        },
                        containerColor = AccentYellow,
                        contentColor = TextPrimary,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Enviar",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Fecha
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimensions.paddingSmall()),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE0E0E0).copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "TODAY",
                            modifier = Modifier.padding(
                                horizontal = Dimensions.paddingSmall(),
                                vertical = 4.dp
                            ),
                            fontSize = Dimensions.fontSizeSmall(),
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }

                // Lista de mensajes
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(Dimensions.paddingMedium()),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall())
                ) {
                    items(messages) { message ->
                        MessageBubble(message)
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isSentByMe) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isSentByMe) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = PrimaryBlue.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (message.isSentByMe) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = if (Dimensions.isCompact()) 280.dp else 350.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (message.isSentByMe) 16.dp else 4.dp,
                    bottomEnd = if (message.isSentByMe) 4.dp else 16.dp
                ),
                color = if (message.isSentByMe) PrimaryBlue else BackgroundWhite,
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    if (message.imageUrl != null) {
                        // Placeholder para imagen
                        Surface(
                            modifier = Modifier
                                .size(200.dp)
                                .padding(bottom = 8.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE0E0E0)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = message.text,
                        fontSize = Dimensions.fontSizeMedium(),
                        color = if (message.isSentByMe) BackgroundWhite else TextPrimary,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message.time,
                    fontSize = Dimensions.fontSizeSmall(),
                    color = TextSecondary.copy(alpha = 0.7f)
                )

                if (message.isSentByMe) {
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Enviado",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
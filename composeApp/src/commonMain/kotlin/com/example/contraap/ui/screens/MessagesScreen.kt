package com.example.contraap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.contraap.ui.theme.*

data class ChatMessage(
    val id: String,
    val name: String,
    val role: String = "",
    val lastMessage: String,
    val time: String,
    val unread: Int,
    val isOnline: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen() {
    var selectedChat by remember { mutableStateOf<ChatMessage?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val messages = remember {
        listOf(
            ChatMessage("1", "Pablo Tzul", "CONTRACTOR", "De acuerdo, estare llegando mañana", "2m ago", 0, true),
            ChatMessage("2", "Karla Barrios", "", "Congusto...", "1h ago", 0, false)
        )
    }

    if (selectedChat != null) {
        ChatDetailScreen(
            chat = selectedChat!!,
            onBackClick = { selectedChat = null }
        )
    } else {
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
                        .padding(Dimensions.paddingMedium())
                ) {
                    Text(
                        text = "Mensajes",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = Dimensions.fontSizeTitle()
                        ),
                        color = TextPrimary
                    )

                    Spacer(Modifier.height(Dimensions.paddingMedium()))

                    // Barra de búsqueda
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "Search contractors or clients",
                                fontSize = Dimensions.fontSizeSmall(),
                                color = TextSecondary.copy(alpha = 0.5f)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = TextSecondary,
                                modifier = Modifier.size(Dimensions.iconSizeMedium())
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = DividerGray,
                            focusedContainerColor = BackgroundWhite,
                            unfocusedContainerColor = Color(0xFFF5F5F5)
                        ),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = Dimensions.fontSizeMedium()
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(Dimensions.paddingSmall()),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(messages) { message ->
                    MessageCard(
                        message = message,
                        onClick = { selectedChat = message }
                    )
                }
            }
        }
    }
}

@Composable
fun MessageCard(
    message: ChatMessage,
    onClick: () -> Unit
) {
    val avatarSize = if (Dimensions.isCompact()) 50.dp else 60.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Dimensions.paddingMedium(),
                    vertical = Dimensions.paddingSmall()
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con indicador online
            Box {
                Surface(
                    modifier = Modifier.size(avatarSize),
                    shape = CircleShape,
                    color = PrimaryBlue.copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = message.name.first().toString(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = if (Dimensions.isCompact()) 18.sp else 20.sp
                            ),
                            color = PrimaryBlue
                        )
                    }
                }

                // Indicador online
                if (message.isOnline) {
                    Surface(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.BottomEnd),
                        shape = CircleShape,
                        color = Color(0xFF4CAF50)
                    ) {}
                }
            }

            Spacer(Modifier.width(Dimensions.paddingSmall()))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = message.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = Dimensions.fontSizeMedium()
                            ),
                            color = TextPrimary,
                            maxLines = 1
                        )

                        if (message.role.isNotEmpty()) {
                            Spacer(Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (message.role == "CONTRACTOR")
                                    PrimaryBlue.copy(alpha = 0.15f)
                                else if (message.role == "BUSINESS")
                                    AccentYellow.copy(alpha = 0.3f)
                                else
                                    Color(0xFFE0E0E0)
                            ) {
                                Text(
                                    text = message.role,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (message.role == "CONTRACTOR")
                                        PrimaryBlue
                                    else if (message.role == "BUSINESS")
                                        Color(0xFF795548)
                                    else
                                        TextSecondary
                                )
                            }
                        }
                    }

                    Text(
                        text = message.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = Dimensions.fontSizeSmall()
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
                        fontSize = Dimensions.fontSizeSmall(),
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

                    if (message.unread > 0) {
                        Spacer(Modifier.width(8.dp))
                        Surface(
                            modifier = Modifier.size(20.dp),
                            shape = CircleShape,
                            color = PrimaryBlue
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = message.unread.toString(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = BackgroundWhite,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    Divider(color = DividerGray.copy(alpha = 0.3f), thickness = 0.5.dp)
}
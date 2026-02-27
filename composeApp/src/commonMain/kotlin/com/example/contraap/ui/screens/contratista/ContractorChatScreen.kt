package com.example.contraap.ui.screens.contractor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contraap.ui.screens.ChatMessage
import com.example.contraap.ui.screens.MessageCard
import com.example.contraap.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractorMessagesScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    val messages = remember {
        listOf(
            ChatMessage("1", "Juan Pérez", "Cliente", "¿A qué hora llegas?", "10:30 AM", 2, true),
            ChatMessage("2", "María García", "", "Gracias por el excelente trabajo", "Ayer", 0, false),
            ChatMessage("3", "Carlos López", "", "¿Puedes venir el martes?", "Hace 2 días", 1, false)
        )
    }

    var selectedChat by remember { mutableStateOf<ChatMessage?>(null) }

    if (selectedChat != null) {
        com.example.contraap.ui.screens.ChatDetailScreen(
            chat = selectedChat!!,
            onBackClick = { selectedChat = null }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Mensajes",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = Dimensions.fontSizeTitle()
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BackgroundWhite
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.paddingMedium()),
                    placeholder = { Text("Buscar clientes") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                )

                LazyColumn {
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
}
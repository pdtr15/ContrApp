package com.example.contraap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.navigationBarsPadding
import com.example.contraap.navigation.BottomNavItem
import com.example.contraap.ui.theme.AccentYellow
import com.example.contraap.ui.theme.TextSecondary

@Composable
fun MainScreen() {

    var selectedRoute by remember { mutableStateOf("home") }
    var showRequestService by remember { mutableStateOf(false) }

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Requests,
        BottomNavItem.Messages,
        BottomNavItem.Profile
    )

    if (showRequestService) {

        RequestServiceScreen(
            onBackClick = { showRequestService = false },
            onSubmit = { showRequestService = false }
        )

    } else {

        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .navigationBarsPadding()
                ) {

                    NavigationBar(
                        containerColor = Color.White.copy(alpha = 0.95f),
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(28.dp)
                            )
                            .background(
                                color = Color.White.copy(alpha = 0.95f),
                                shape = RoundedCornerShape(28.dp)
                            )
                            .height(64.dp)
                    ) {

                        items.forEach { item ->

                            val selected = selectedRoute == item.route

                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                selected = selected,
                                onClick = { selectedRoute = item.route },
                                alwaysShowLabel = true,
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = AccentYellow,
                                    selectedTextColor = AccentYellow,
                                    unselectedIconColor = TextSecondary.copy(alpha = 0.6f),
                                    unselectedTextColor = TextSecondary.copy(alpha = 0.6f),
                                    indicatorColor = AccentYellow.copy(alpha = 0.15f)
                                )
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->

            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                when (selectedRoute) {
                    "home" -> HomeScreen(
                        onRequestServiceClick = { showRequestService = true }
                    )
                    "requests" -> RequestsScreen()
                    "messages" -> MessagesScreen()
                    "profile" -> ProfileScreen()
                }
            }
        }
    }
}
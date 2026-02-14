package com.example.contraap.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.contraap.navigation.BottomNavItem
import com.example.contraap.ui.theme.PrimaryBlue
import com.example.contraap.ui.theme.AccentYellow
import com.example.contraap.ui.theme.TextSecondary
import com.example.contraap.ui.theme.BackgroundWhite

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var showRequestService by remember { mutableStateOf(false) }  // ← Agregado estado

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Requests,
        BottomNavItem.Messages,
        BottomNavItem.Profile
    )

    // ← Agregado: Mostrar RequestServiceScreen cuando sea necesario
    if (showRequestService) {
        RequestServiceScreen(
            onBackClick = { showRequestService = false },
            onSubmit = {
                showRequestService = false
                // TODO: Aquí puedes agregar lógica para guardar la solicitud
            }
        )
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = BackgroundWhite,
                    contentColor = PrimaryBlue,
                    tonalElevation = 8.dp
                ) {
                    items.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true

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
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AccentYellow,
                                selectedTextColor = AccentYellow,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = AccentYellow.copy(alpha = 0.2f)
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(BottomNavItem.Home.route) {
                    HomeScreen(
                        onRequestServiceClick = { showRequestService = true }  // ← Agregado callback
                    )
                }
                composable(BottomNavItem.Requests.route) {
                    RequestsScreen()
                }
                composable(BottomNavItem.Messages.route) {
                    MessagesScreen()
                }
                composable(BottomNavItem.Profile.route) {
                    ProfileScreen()
                }
            }
        }
    }
}
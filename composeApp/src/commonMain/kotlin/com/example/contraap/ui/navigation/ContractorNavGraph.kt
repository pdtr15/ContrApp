package com.example.contraap.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.contraap.ui.screens.contractor.ContractorDashboardScreen
import com.example.contraap.ui.screens.contractor.ContractorMessagesScreen
import com.example.contraap.ui.screens.contractor.ContractorProfileScreen
import com.example.contraap.ui.screens.contractor.EarningsScreen
import com.example.contraap.ui.screens.contractor.JobRequestDetailScreen
import com.example.contraap.ui.screens.contractor.JobsScreen

private val ButtonYellow    = Color(0xFFFFCA28)
private val TextGray        = Color(0xFF9098B1)
private val SelectedBg      = Color(0xFFFFF8E1) // fondo amarillo suave del tab activo

object Routes {
    const val DASHBOARD          = "contractor_dashboard"
    const val MESSAGES           = "contractor_messages"
    const val JOBS               = "jobs_screen"
    const val EARNINGS           = "earnings_screen"
    const val PROFILE            = "contractor_profile"
    const val JOB_DETAIL         = "job_detail/{jobId}"
    const val JOB_REQUEST_DETAIL = "job_request_detail"

    fun jobDetail(jobId: String) = "job_detail/$jobId"
}

private data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

private val bottomItems = listOf(
    BottomNavItem(Routes.DASHBOARD, Icons.Default.Dashboard,   "Panel"),
    BottomNavItem(Routes.MESSAGES,  Icons.Default.Chat,        "Mensajes"),
    BottomNavItem(Routes.JOBS,      Icons.Default.Work,        "Trabajos"),
    BottomNavItem(Routes.EARNINGS,  Icons.Default.AttachMoney, "Ganancias"),
    BottomNavItem(Routes.PROFILE,   Icons.Default.Person,      "Perfil")
)

private val rootRoutes = bottomItems.map { it.route }.toSet()

@Composable
fun ContractorNavGraph() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val currentRoute = currentDestination?.route

    val showBottomBar = currentRoute in rootRoutes

    Scaffold(
        containerColor = Color(0xFFF5F7FA),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 0.dp,         // sin sombra de tono
                    modifier = Modifier
                ) {
                    bottomItems.forEach { item ->
                        val selected = currentDestination
                            ?.hierarchy
                            ?.any { it.route == item.route } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(Routes.DASHBOARD) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector        = item.icon,
                                    contentDescription = item.label,
                                    modifier           = Modifier.padding(bottom = 2.dp)
                                )
                            },
                            label = {
                                Text(
                                    text       = item.label,
                                    fontSize   = 10.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor   = ButtonYellow,
                                selectedTextColor   = ButtonYellow,
                                indicatorColor      = SelectedBg,   // fondo redondeado suave
                                unselectedIconColor = TextGray,
                                unselectedTextColor = TextGray
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Routes.DASHBOARD,
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable(Routes.DASHBOARD) {
                ContractorDashboardScreen(navController = navController)
            }
            composable(Routes.MESSAGES) {
                ContractorMessagesScreen(navController = navController)
            }
            composable(Routes.JOBS) {
                JobsScreen(navController = navController)
            }
            composable(Routes.EARNINGS) {
                EarningsScreen()
            }
            composable(Routes.PROFILE) {
                ContractorProfileScreen()
            }
            composable(Routes.JOB_DETAIL) { backStack ->
                val jobId = backStack.arguments?.getString("jobId") ?: ""
                JobRequestDetailScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Routes.JOB_REQUEST_DETAIL) {
                JobRequestDetailScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
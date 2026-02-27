package com.example.contraap

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contraap.ui.screens.*
import com.example.contraap.ui.onboarding.OnboardingScreen
import com.example.contraap.ui.splash.SplashScreen
import com.example.contraap.ui.theme.ContraTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.contraap.ui.navigation.ContractorNavGraph  // ← NUEVO
import com.example.contraap.data.models.UserRole

@Composable
fun App() {
    ContraTheme {

        val navController = rememberNavController()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            NavHost(
                navController = navController,
                startDestination = "onboarding"
            ) {

                composable("onboarding") {
                    OnboardingScreen(
                        onFinish = {
                            navController.navigate("splash") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    )
                }

                composable("splash") {
                    SplashScreen(
                        onFinish = {
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    )
                }

                composable("login") {
                    LoginScreen(
                        onLoginSuccess = { role ->
                            if (role == UserRole.CONTRATISTA) {
                                navController.navigate("mainContratista") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                navController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        },
                        onRegisterProfesionalClick = {
                            navController.navigate("registerProfesional")
                        },
                        onRegisterClienteClick = {
                            navController.navigate("registerCliente")
                        }
                    )
                }

                composable("registerProfesional") {
                    RegisterScreen(
                        role = UserRole.CONTRATISTA,
                        onBack = { navController.popBackStack() },
                        onRegisterSuccess = {
                            navController.navigate("mainContratista") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                }

                composable("main") {
                    MainScreen()
                }

                composable("registerCliente") {
                    JoinContrAppScreen(
                        role = UserRole.CLIENTE,
                        onBack = { navController.popBackStack() },
                        onRegisterSuccess = {
                            navController.navigate("main") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                }

                composable("mainContratista") {
                    ContractorNavGraph()  // ← CAMBIADO: antes era ContractorDashboardScreen
                }

                // ← ELIMINADO: job_detail ya vive dentro de ContractorNavGraph
            }
        }
    }
}
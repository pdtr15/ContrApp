package com.example.contraap

import androidx.compose.runtime.*
import com.example.contraap.data.models.UserRole
import com.example.contraap.ui.onboarding.OnboardingScreen
import com.example.contraap.ui.splash.SplashScreen
import com.example.contraap.ui.screens.*
import com.example.contraap.ui.theme.ContraTheme

@Composable
fun App() {

    var showOnboarding by remember { mutableStateOf(true) }
    var showSplash by remember { mutableStateOf(false) }
    var showLogin by remember { mutableStateOf(false) }
    var showRegister by remember { mutableStateOf(false) }
    var showJoin by remember { mutableStateOf(false) }
    var showMain by remember { mutableStateOf(false) }

    ContraTheme {

        when {

            showOnboarding -> OnboardingScreen(
                onFinish = {
                    showOnboarding = false
                    showSplash = true
                }
            )

            showSplash -> SplashScreen(
                onFinish = {
                    showSplash = false
                    showLogin = true
                }
            )

            showLogin -> LoginScreen(
                onLoginSuccess = {
                    showLogin = false
                    showMain = true
                },
                onRegisterProfesionalClick = {
                    showLogin = false
                    showRegister = true
                },
                onRegisterClienteClick = {
                    showLogin = false
                    showJoin = true
                }
            )

            // 🔵 REGISTRO CONTRATISTA
            showRegister -> RegisterScreen(
                role = UserRole.CONTRATISTA,
                onBack = {
                    showRegister = false
                    showLogin = true
                },
                onRegisterSuccess = {
                    showRegister = false
                    showMain = true
                }
            )

            // 🟡 REGISTRO CLIENTE
            showJoin -> JoinContrAppScreen(
                role = UserRole.CLIENTE,   // 🔥 AQUÍ ESTABA EL ERROR
                onBack = {
                    showJoin = false
                    showLogin = true
                },
                onRegisterSuccess = {
                    showJoin = false
                    showMain = true
                }
            )

            showMain -> MainScreen()
        }
    }
}
package com.example.contraap

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// Asegúrate de importar tus pantallas
import com.example.contraap.ui.screens.*
import com.example.contraap.ui.onboarding.OnboardingScreen
import com.example.contraap.ui.splash.SplashScreen
import com.example.contraap.ui.theme.ContraTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun App() {
    var showOnboarding by remember { mutableStateOf(true) }
    var showSplash by remember { mutableStateOf(false) }
    var showLogin by remember { mutableStateOf(false) }
    var showRegister by remember { mutableStateOf(false) }
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
                    showRegister = true
                }
            )
            showRegister -> RegisterScreen(
                onBack = {
                    showRegister = false
                    showLogin = true
                },
                onRegisterSuccess = {
                    showRegister = false
                    showMain = true
                }
            )
            showMain -> MainScreen()
        }
    }
}
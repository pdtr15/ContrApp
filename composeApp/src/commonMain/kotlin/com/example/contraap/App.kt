import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// Asegúrate de importar tus pantallas
import com.example.contraap.ui.screens.*
import com.example.contraap.ui.onboarding.OnboardingScreen
import com.example.contraap.ui.splash.SplashScreen
import com.example.contraap.ui.theme.ContraTheme

@Composable
fun App() {
    ContraTheme {
        // 1. EL CONTROLADOR (El taxista)
        val navController = rememberNavController()

        // 2. EL MAPA (NavHost)
        // startDestination = "onboarding" significa que es la primera pantalla que se ve
        NavHost(navController = navController, startDestination = "onboarding") {

            // --- PANTALLA 1: ONBOARDING ---
            composable("onboarding") {
                OnboardingScreen(
                    onFinish = {
                        // Navegamos al Splash y borramos el Onboarding del historial
                        // para que no se pueda volver atrás.
                        navController.navigate("splash") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                )
            }

            // --- PANTALLA 2: SPLASH ---
            composable("splash") {
                SplashScreen(
                    onFinish = {
                        // Del Splash saltamos al Login, borrando el Splash del historial
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }

            // --- PANTALLA 3: LOGIN ---
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        // Al entrar con éxito, vamos al Home (Main)
                        // Borramos TODO el historial anterior para que al dar "Atrás" se salga de la app
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onRegisterProfesionalClick = {
                        // Simplemente vamos a la pantalla de registro
                        navController.navigate("registerProfesional")
                    },
                    onRegisterClienteClick = {
                        // Simplemente vamos a la pantalla de registro
                        navController.navigate("registerCliente")
                    }
                )
            }

            // --- PANTALLA 4: REGISTRO ---
            composable("registerProfesional") {
                // Aquí usamos tu pantalla tal cual la dejaste, con el evento onBack
                RegisterScreen(
                    onBack = {
                        // Esta es la magia: "Regresa a la pantalla anterior" (Login)
                        navController.popBackStack()
                    }
                )
            }

            // --- PANTALLA 5: MAIN (HOME) ---
            composable("main") {
                MainScreen()
            }

            // --- PANTALLA 6: REGISTRO ---
            composable("registerCliente") {
                // Aquí usamos tu pantalla tal cual la dejaste, con el evento onBack
                JoinContraAppScreen(
                    onBack = {
                        // Esta es la magia: "Regresa a la pantalla anterior" (Login)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
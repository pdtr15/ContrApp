import androidx.compose.runtime.*
import com.example.contraap.ui.screens.RegisterScreen
import com.example.contraap.ui.screens.LoginScreen
import com.example.contraap.ui.screens.MainScreen
import com.example.contraap.ui.theme.ContraTheme
import com.example.contraap.ui.onboarding.OnboardingScreen
import com.example.contraap.ui.splash.SplashScreen

@Composable
fun App() {
    var showOnboarding by remember { mutableStateOf(true) }
    var showSplash by remember { mutableStateOf(false) }
    var showLogin by remember { mutableStateOf(false) }
    var showMain by remember { mutableStateOf(false) }  // ← Agregado

    ContraTheme {
        when {
            showOnboarding -> {
                OnboardingScreen(
                    onFinish = {
                        showOnboarding = false
                        showSplash = true
                    }
                )
            }

            showSplash -> {
                SplashScreen(
                    onFinish = {
                        showSplash = false
                        showLogin = true
                    }
                )
            }

            showLogin -> {
                LoginScreen(
                    onLoginSuccess = {
                        showLogin = false
                        showMain = true  // ← Cambiado
                    },
                    onRegisterClick = {
                        showLogin = false
                        // TODO: Mostrar RegisterScreen
                    }
                )
            }

            showMain -> {  // ← Agregado este caso
                MainScreen()
            }

            else -> {
                RegisterScreen()
            }
        }
    }
}
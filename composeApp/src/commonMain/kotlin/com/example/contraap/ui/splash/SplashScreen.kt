package com.example.contraap.ui.splash

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import contraap.composeapp.generated.resources.Res
import contraap.composeapp.generated.resources.contraap
import com.example.contraap.ui.theme.AccentYellow
import com.example.contraap.ui.theme.BackgroundWhite
import com.example.contraap.ui.theme.TextPrimary

@Composable
fun SplashScreen(
    onFinish: () -> Unit
) {
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        val duration = 2500L
        val steps = 50
        val stepDelay = duration / steps

        repeat(steps) {
            delay(stepDelay)
            progress = (it + 1) / steps.toFloat()
        }

        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite), // Fondo blanco
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // Logo
            Image(
                painter = painterResource(Res.drawable.contraap),
                contentDescription = "ContrApp Logo",
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(400.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Subtítulo amarillo
            Text(
                text = "Tu profesional ideal",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 18.sp
                ),
                color = TextPrimary.copy(alpha = 0.8f) // Amarillo con transparencia
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Indicador de progreso amarillo
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .width(240.dp)
                    .height(3.dp),
                color = AccentYellow, // Amarillo
                trackColor = AccentYellow.copy(alpha = 0.2f), // Amarillo claro
            )
        }
    }
}
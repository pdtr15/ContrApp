package com.example.contraap.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ContraColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    secondary = AccentYellow,
    onSecondary = Color.Black,
    background = BackgroundWhite,
    surface = BackgroundWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun ContraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ContraColorScheme,
        content = content
    )
}
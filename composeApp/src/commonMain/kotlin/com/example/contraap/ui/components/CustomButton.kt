package com.example.contraap.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null, // Hacemos el icono opcional
    buttonColor: Color = Color(0xFFFFC107), // Color por defecto (tu amarillo)
    textColor: Color = Color.Black,
    iconTint: Color = Color.Black,
    height: Dp = 56.dp, // Altura estándar, un poco más alta para Material Design
    cornerRadius: Dp = 12.dp, // Radio de las esquinas
    enabled: Boolean = true // Por si quieres deshabilitarlo
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            disabledContainerColor = buttonColor.copy(alpha = 0.5f) // Estilo para deshabilitado
        ),
        enabled = enabled
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center, // Centrar contenido dentro del botón
            modifier = Modifier.fillMaxWidth() // Asegura que el Row ocupe todo el ancho para centrar
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            // Solo mostramos el icono si no es nulo
            if (icon != null) {
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = null, // La descripción de contenido debe ser más específica para accesibilidad
                    tint = iconTint,
                    modifier = Modifier.size(20.dp) // Tamaño del icono para que se vea bien
                )
            }
        }
    }
}
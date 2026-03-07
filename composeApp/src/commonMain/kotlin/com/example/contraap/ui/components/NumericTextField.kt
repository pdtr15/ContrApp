package com.example.contraap.ui.components // Ajusta esto a tu paquete real

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun NumericTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    maxLength: Int
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // Filtramos para que solo queden números
            val numericString = newValue.filter { it.isDigit() }

            // Solo actualizamos si no nos pasamos del límite
            if (numericString.length <= maxLength) {
                onValueChange(numericString)
            }
        },
        label = { Text(label) },
        trailingIcon = { Icon(icon, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        // Forzamos el teclado numérico siempre
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}
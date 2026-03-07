package com.example.contraap.ui.components // Ajusta tu paquete

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PhoneTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // Filtramos letras y limitamos a 8 dígitos exactos
            val numericString = newValue.filter { it.isDigit() }
            if (numericString.length <= 8) {
                onValueChange(numericString)
            }
        },
        label = { Text(label) },
        // Agregamos el +502 fijo al inicio para que se vea súper pro
        prefix = { Text("+502 ") },
        trailingIcon = { Icon(icon, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        // Aquí ocurre la magia: Agrega el guion visualmente (1234-5678)
        visualTransformation = GuatePhoneVisualTransformation()
    )
}

// --- CLASE QUE PONE EL GUION AUTOMÁTICAMENTE ---
class GuatePhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Convierte "12345678" en "1234-5678" solo para la pantalla
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 3) out += "-" // Pone el guion después del cuarto número
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 8) return offset + 1
                return 9
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                return 8
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}
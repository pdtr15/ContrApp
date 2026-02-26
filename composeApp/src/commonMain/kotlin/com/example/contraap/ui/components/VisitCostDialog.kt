import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Asegúrate de tener tus colores definidos o importarlos
private val CardBlue = Color(0xFF4FC3F7)
private val TextGray = Color(0xFF9098B1)

@Composable
fun VisitCostDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit // Pasamos el monto escrito de vuelta
) {
    // El estado del texto vive dentro del modal ahora
    var visitAmount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Costo de Visita", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    "Ingresa el monto que cobrarás por ir a revisar el problema (solo revisión).",
                    fontSize = 14.sp,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = visitAmount,
                    onValueChange = { visitAmount = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Monto (MXN)") },
                    leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(visitAmount) // Enviamos el monto al padre
                },
                colors = ButtonDefaults.buttonColors(containerColor = CardBlue)
            ) {
                Text("Enviar Propuesta", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextGray)
            }
        },
        containerColor = Color.White
    )
}
package com.example.contraap.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(
    label: String,
    placeholder: String,
    opciones: List<String>,
    selectedItems: List<String>, // Cambiado de String a List<String>
    onSelectionChange: (List<String>) -> Unit, // Callback devuelve la nueva lista
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    // Texto a mostrar en la caja (ej: "Plomero, Electricista")
    val displayText = if (selectedItems.isEmpty()) {
        ""
    } else {
        selectedItems.joinToString(", ")
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = displayText,
                onValueChange = {},
                placeholder = { Text(text = placeholder) },
                readOnly = true, // Solo lectura para evitar escribir manual
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1976D2), // Tu azul
                    unfocusedBorderColor = Color.Gray
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                opciones.forEach { opcion ->
                    // Verificamos si esta opción ya está seleccionada
                    val isSelected = selectedItems.contains(opcion)

                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Checkbox para indicar selección múltiple
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = null // El click lo maneja el padre
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = opcion)
                            }
                        },
                        onClick = {
                            // Lógica de Agregar/Quitar
                            val newList = if (isSelected) {
                                selectedItems - opcion // Si ya está, lo quita
                            } else {
                                selectedItems + opcion // Si no está, lo agrega
                            }
                            onSelectionChange(newList)
                            // NOTA: No cerramos el menú (expanded = false) para permitir múltiples selecciones
                        },
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                    )
                }
            }
        }
    }
}
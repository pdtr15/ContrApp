package com.example.contraap.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
actual fun DocumentPickerBox(
    selectedFileName: String?,
    onFileSelected: (fileName: String, fileBytes: ByteArray) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .border(1.dp, Color(0xFF64B5F6), RoundedCornerShape(12.dp))
            .background(Color(0xFFE3F2FD), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.CloudUpload,
                contentDescription = null,
                tint = Color(0xFF1976D2),
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (selectedFileName != null) "✓ $selectedFileName" else "Sube tu DPI o RTU aquí",
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1976D2)
            )
            if (selectedFileName == null) {
                Text("PDF, JPG o PNG (Max. 5MB)", fontSize = 12.sp, color = Color(0xFF64B5F6))
            }
        }
    }
}
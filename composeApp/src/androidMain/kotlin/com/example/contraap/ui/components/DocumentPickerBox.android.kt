package com.example.contraap.ui.components

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun DocumentPickerBox(
    selectedFileName: String?,
    onFileSelected: (fileName: String, fileBytes: ByteArray) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    var tempUri by remember { mutableStateOf<Uri?>(null) }
    var pendingCameraLaunch by remember { mutableStateOf(false) }

    fun processUri(uri: Uri, fallback: String) {
        val name = getFileName(context, uri) ?: fallback
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes() ?: return
        onFileSelected(name, bytes)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { processUri(it, "imagen.jpg") } }

    val pdfLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { processUri(it, "documento.pdf") } }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) tempUri?.let { processUri(it, "foto.jpg") }
    }

    val cameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            pendingCameraLaunch = true
        }
    }

    val storagePermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) galleryLauncher.launch("image/*")
    }

    // Lanza la cámara DESPUÉS de que el BottomSheet se cierre
    LaunchedEffect(showBottomSheet, pendingCameraLaunch) {
        if (!showBottomSheet && pendingCameraLaunch) {
            android.util.Log.d("CAMERA_DEBUG", "LaunchedEffect: lanzando cameraLauncher...")
            pendingCameraLaunch = false
            try {
                val uri = createTempUri(context)
                tempUri = uri
                android.util.Log.d("CAMERA_DEBUG", "Uri creado: $uri")
                cameraLauncher.launch(uri)
                android.util.Log.d("CAMERA_DEBUG", "cameraLauncher.launch() ejecutado")
            } catch (e: Exception) {
                android.util.Log.e("CAMERA_DEBUG", "Error: ${e.message}")
            }
        }
    }

    fun openCamera() {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        android.util.Log.d("CAMERA_DEBUG", "hasPermission: $hasPermission")

        if (hasPermission) {
            android.util.Log.d("CAMERA_DEBUG", "Tiene permiso, lanzando camara...")
            pendingCameraLaunch = true
            showBottomSheet = false
        } else {
            android.util.Log.d("CAMERA_DEBUG", "Sin permiso, pidiendo...")
            showBottomSheet = false
            cameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .border(
                width = 1.dp,
                color = if (selectedFileName != null) Color(0xFF1976D2) else Color(0xFF64B5F6),
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = if (selectedFileName != null) Color(0xFFBBDEFB) else Color(0xFFE3F2FD),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { showBottomSheet = true },
        contentAlignment = Alignment.Center
    ) {
        if (selectedFileName == null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.CloudUpload, null, tint = Color(0xFF1976D2), modifier = Modifier.size(32.dp))
                Spacer(Modifier.height(4.dp))
                Text("Sube tu DPI o RTU aquí", fontWeight = FontWeight.Medium, color = Color(0xFF1976D2))
                Text("PDF, JPG o PNG (Max. 5MB)", fontSize = 12.sp, color = Color(0xFF64B5F6))
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF1976D2), modifier = Modifier.size(32.dp))
                Spacer(Modifier.height(4.dp))
                Text("Archivo seleccionado ✓", fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                Text(selectedFileName, fontSize = 11.sp, color = Color(0xFF64B5F6), maxLines = 1)
                Text("Toca para cambiar", fontSize = 10.sp, color = Color(0xFF90CAF9))
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    "Seleccionar documento",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ListItem(
                    headlineContent = { Text("Tomar foto") },
                    leadingContent = { Icon(Icons.Default.CameraAlt, null) },
                    modifier = Modifier.clickable {
                        openCamera()
                    }
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text("Elegir de galería") },
                    leadingContent = { Icon(Icons.Default.PhotoLibrary, null) },
                    modifier = Modifier.clickable {
                        showBottomSheet = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            galleryLauncher.launch("image/*")
                        } else {
                            storagePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text("Seleccionar PDF") },
                    leadingContent = { Icon(Icons.Default.PictureAsPdf, null) },
                    modifier = Modifier.clickable {
                        showBottomSheet = false
                        pdfLauncher.launch("application/pdf")
                    }
                )
            }
        }
    }
}

private fun createTempUri(context: Context): Uri {
    val storageDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
    val file = File.createTempFile(
        "temp_${System.currentTimeMillis()}",
        ".jpg",
        storageDir
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}

private fun getFileName(context: Context, uri: Uri): String? {
    return try {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val idx = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(idx)
        }
    } catch (_: Exception) { null }
}
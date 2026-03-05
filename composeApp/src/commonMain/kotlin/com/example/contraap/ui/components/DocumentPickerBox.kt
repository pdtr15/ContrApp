package com.example.contraap.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun DocumentPickerBox(
    selectedFileName: String?,
    onFileSelected: (fileName: String, fileBytes: ByteArray) -> Unit,
    modifier: Modifier = Modifier
)
package com.example.contraap.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import com.example.contraap.ui.theme.AccentYellow
import com.example.contraap.ui.theme.TextPrimary
import com.example.contraap.ui.theme.Dimensions

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimensions.buttonHeight()),
        enabled = enabled,
        shape = RoundedCornerShape(Dimensions.paddingMedium()),
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentYellow,
            contentColor = TextPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = Dimensions.cardElevation(),
            pressedElevation = Dimensions.cardElevation() * 2
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = Dimensions.fontSizeMedium()
            )
        )
    }
}
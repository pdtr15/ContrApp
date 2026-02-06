package com.example.contraap.ui.onboarding

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import com.example.contraap.ui.components.PrimaryButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import org.jetbrains.compose.resources.painterResource
import com.example.contraap.ui.theme.AccentYellow

@Composable
fun OnboardingPage(
    title: String,
    description: String,
    buttonText: String,
    pageIndex: Int,
    totalPages: Int,
    imageResource: org.jetbrains.compose.resources.DrawableResource,
    onNext: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            // Imagen sin Card - solo la imagen
            Image(
                painter = painterResource(imageResource),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(48.dp))

            // Título
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    lineHeight = 36.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Descripción
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 24.sp
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(Modifier.weight(1f))

            // Indicadores de página
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                repeat(totalPages) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .width(if (index == pageIndex) 32.dp else 10.dp)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(
                                color = if (index == pageIndex)
                                    AccentYellow
                                else
                                    AccentYellow.copy(alpha = 0.2f)
                            )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Botón
            PrimaryButton(
                text = buttonText,
                onClick = onNext
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}
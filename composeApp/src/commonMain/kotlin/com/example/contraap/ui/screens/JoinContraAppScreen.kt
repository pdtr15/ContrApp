package com.example.contraap.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contraap.ui.components.CustomButton
import com.example.contraap.ui.components.CustomOutlinedTextField
import com.example.contraap.ui.components.CustomTopAppBar
import com.example.contraap.ui.components.SocialButton
import com.example.contraap.viewmodel.JoinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinContraAppScreen(
    onBack: () -> Unit
) {
    val viewModel = remember { JoinViewModel() }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "CONTRAAPP",
                onBackClick = { onBack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                color = Color(0xFFFFC107),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Logo",
                        modifier = Modifier.size(60.dp),
                        tint = Color.White
                    )
                }
            }

            Text(
                text = "Únete a CONTRAAPP",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Encuentra a los mejores contratistas para tu hogar o negocio.",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            CustomOutlinedTextField(
                value = viewModel.nombre,
                onValueChange = { viewModel.nombre = it },
                label = "Nombre Completo",
                placeholder = "Ej. Juan Pérez",
                icon = Icons.Default.Person
            )

            CustomOutlinedTextField(
                value = viewModel.correo,
                onValueChange = { viewModel.correo = it },
                label = "Correo Electrónico",
                placeholder = "ejemplo@correo.com",
                icon = Icons.Default.Email
            )

            CustomOutlinedTextField(
                value = viewModel.telefono,
                onValueChange = { viewModel.telefono = it },
                label = "Teléfono",
                placeholder = "+502 0000 0000",
                icon = Icons.Default.Phone
            )

            CustomOutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = "Contraseña",
                placeholder = "Crea una contraseña",
                icon = Icons.Default.Visibility,
                isPassword = true
            )

            CustomButton(
                text = "Crear Cuenta",
                onClick = { viewModel.onCrearCuenta() },
                buttonColor = Color(0xFFFFC107)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                Text("  o regístrate con  ", color = Color.Gray, fontSize = 12.sp)
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SocialButton(
                    text = "Google",
                    icon = rememberVectorPainter(Icons.Default.AccountCircle), // Placeholder
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
                SocialButton(
                    text = "Facebook",
                    icon = rememberVectorPainter(Icons.Default.Facebook),
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFF1877F2),
                    textColor = Color.White
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Ya tienes una cuenta? ", fontSize = 14.sp, color = Color.Gray)
                TextButton(onClick = { }, contentPadding = PaddingValues(0.dp)) {
                    Text("Iniciar sesión", color = Color(0xFFFFC107), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
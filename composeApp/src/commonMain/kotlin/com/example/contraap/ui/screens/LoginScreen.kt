package com.example.contraap.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import contraap.composeapp.generated.resources.Res
import contraap.composeapp.generated.resources.login
import com.example.contraap.ui.components.PrimaryButton
import com.example.contraap.ui.theme.*
import com.example.contraap.viewmodel.LoginViewModel
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onRegisterProfesionalClick: () -> Unit = {},
    onRegisterClienteClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var mostrarModalRoles by remember { mutableStateOf(false) }

    // Navegar cuando el login es exitoso
    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
            onLoginSuccess()
            viewModel.resetLoginSuccess()
        }
    }

    // Contenido principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()), // Habilita el scroll si el teclado tapa
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))

        // Logo
        Image(
            painter = painterResource(Res.drawable.login),
            contentDescription = "ContrApp Logo",
            modifier = Modifier.size(170.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "ContrApp",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 38.sp,
                letterSpacing = 1.sp
            ),
            color = PrimaryBlue
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Bienvenido de nuevo",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            ),
            color = TextPrimary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Ingresa tus credenciales para continuar",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(32.dp))

        // --- CAMPOS DE TEXTO ---

        // Email
        Text(
            text = "Email",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = TextPrimary,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("correo@ejemplo.com", color = TextSecondary.copy(alpha = 0.5f)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = DividerGray
            ),
            isError = uiState.errorMessage != null && uiState.email.isBlank()
        )

        Spacer(Modifier.height(20.dp))

        // Password
        Text(
            text = "Contraseña",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = TextPrimary,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("• • • • • • • •", color = TextSecondary.copy(alpha = 0.5f)) },
            singleLine = true,
            visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = DividerGray
            ),
            trailingIcon = {
                IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                    Icon(
                        imageVector = if (uiState.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle password",
                        tint = TextSecondary
                    )
                }
            },
            isError = uiState.errorMessage != null && uiState.password.isBlank()
        )

        if (uiState.errorMessage != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "¿Olvidaste tu contraseña?",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            color = PrimaryBlue,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onForgotPasswordClick() }
        )

        Spacer(Modifier.height(32.dp))

        // Botón Login
        PrimaryButton(
            text = if (uiState.isLoading) "Cargando..." else "Ingresar",
            onClick = { viewModel.onLoginClick() }
        )

        Spacer(Modifier.height(24.dp))

        // Separador Social
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = DividerGray)
            Text(
                text = "O CONTINUAR CON",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, fontWeight = FontWeight.Medium),
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = DividerGray)
        }

        Spacer(Modifier.height(24.dp))

        // Botones Redes Sociales
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SocialButton(text = "Google", letter = "G", onClick = { viewModel.onGoogleLogin() })
            SocialButton(text = "Facebook", letter = "f", color = PrimaryBlue, onClick = { viewModel.onFacebookLogin() })
        }

        Spacer(Modifier.height(40.dp)) // Espacio fijo en lugar de weight(1f)

        // Footer Registro
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "¿No tienes una cuenta? ", color = TextSecondary)
            Text(
                text = "Regístrate",
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue,
                modifier = Modifier.clickable { mostrarModalRoles = true }
            )
        }

        Spacer(Modifier.height(24.dp))
    }

    // --- MODAL BOTTOM SHEET (Aquí está la lógica nueva) ---
    if (mostrarModalRoles) {
        ModalBottomSheet(
            onDismissRequest = { mostrarModalRoles = false },
            containerColor = BackgroundWhite,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Selecciona tu perfil",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = "¿Cómo deseas usar ContraApp?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botón Profesional (Usa tu PrimaryButton)
                Button(
                    onClick = {
                        mostrarModalRoles = false
                        onRegisterProfesionalClick() // TODO: Aquí podrías pasar "PROFESIONAL"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Icon(Icons.Default.Build, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(12.dp))
                    Text("Registrarme como Profesional", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                // Botón Cliente (Estilo Outlined)
                OutlinedButton(
                    onClick = {
                        mostrarModalRoles = false
                        onRegisterClienteClick() // TODO: Aquí podrías pasar "CLIENTE"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(PrimaryBlue))
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryBlue)
                    Spacer(Modifier.width(12.dp))
                    Text("Registrarme como Cliente", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                }
            }
        }
    }
}

// Pequeño componente auxiliar para limpiar el código de los botones sociales
@Composable
fun RowScope.SocialButton(text: String, letter: String, color: Color = Color.Black, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.weight(1f).height(52.dp),
        shape = RoundedCornerShape(12.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp, brush = androidx.compose.ui.graphics.SolidColor(Color.LightGray))
    ) {
        Text(text = letter, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 14.sp, color = Color.Black)
    }
}
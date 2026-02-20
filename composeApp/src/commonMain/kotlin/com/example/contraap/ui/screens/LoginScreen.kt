package com.example.contraap.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

    // Contenido principal con scroll
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .verticalScroll(rememberScrollState())
            .padding(Dimensions.paddingMedium()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(Dimensions.paddingLarge()))

        // Logo responsivo
        Image(
            painter = painterResource(Res.drawable.login),
            contentDescription = "ContrApp Logo",
            modifier = Modifier.size(
                if (Dimensions.isCompact()) 140.dp
                else if (Dimensions.isMedium()) 170.dp
                else 200.dp
            ),
            contentScale = ContentScale.Fit
        )

        Spacer(Modifier.height(Dimensions.paddingMedium()))

        // Título responsivo
        Text(
            text = "ContrApp",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = Dimensions.fontSizeHeadline(),
                letterSpacing = 1.sp
            ),
            color = PrimaryBlue
        )

        Spacer(Modifier.height(Dimensions.paddingLarge()))

        Text(
            text = "Bienvenido de nuevo",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = Dimensions.fontSizeTitle()
            ),
            color = TextPrimary
        )

        Spacer(Modifier.height(Dimensions.paddingSmall()))

        Text(
            text = "Ingresa tus credenciales para continuar",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = Dimensions.fontSizeMedium()
            ),
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(Dimensions.paddingLarge()))

        // Campo Email
        Text(
            text = "Email",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = Dimensions.fontSizeSmall()
            ),
            color = TextPrimary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimensions.paddingSmall()))

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "correo@ejemplo.com",
                    color = TextSecondary.copy(alpha = 0.5f),
                    fontSize = Dimensions.fontSizeMedium()
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = DividerGray,
                focusedContainerColor = BackgroundWhite,
                unfocusedContainerColor = BackgroundWhite
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = Dimensions.fontSizeMedium()
            ),
            isError = uiState.errorMessage != null && uiState.email.isBlank()
        )

        Spacer(Modifier.height(Dimensions.paddingMedium()))

        // Campo Password
        Text(
            text = "Contraseña",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = Dimensions.fontSizeSmall()
            ),
            color = TextPrimary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimensions.paddingSmall()))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "• • • • • • • •",
                    color = TextSecondary.copy(alpha = 0.5f),
                    fontSize = Dimensions.fontSizeMedium()
                )
            },
            singleLine = true,
            visualTransformation = if (uiState.isPasswordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = DividerGray,
                focusedContainerColor = BackgroundWhite,
                unfocusedContainerColor = BackgroundWhite
            ),
            trailingIcon = {
                IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                    Icon(
                        imageVector = if (uiState.isPasswordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                        contentDescription = if (uiState.isPasswordVisible)
                            "Ocultar contraseña"
                        else
                            "Mostrar contraseña",
                        tint = TextSecondary
                    )
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = Dimensions.fontSizeMedium()
            ),
            isError = uiState.errorMessage != null && uiState.password.isBlank()
        )

        // Mostrar error si existe
        if (uiState.errorMessage != null) {
            Spacer(Modifier.height(Dimensions.paddingSmall()))
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = Dimensions.fontSizeSmall()
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(Dimensions.paddingSmall()))

        Text(
            text = "¿Olvidaste tu contraseña?",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = Dimensions.fontSizeSmall()
            ),
            color = PrimaryBlue,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onForgotPasswordClick() }
        )

        Spacer(Modifier.height(Dimensions.paddingLarge()))

        // Botón Login
        PrimaryButton(
            text = if (uiState.isLoading) "Cargando..." else "Ingresar",
            onClick = { viewModel.onLoginClick() },
            enabled = !uiState.isLoading
        )

        Spacer(Modifier.height(Dimensions.paddingMedium()))

        // Separador Social
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = DividerGray)
            Text(
                text = "O CONTINUAR CON",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = Dimensions.fontSizeSmall(),
                    fontWeight = FontWeight.Medium
                ),
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = Dimensions.paddingMedium())
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = DividerGray)
        }

        Spacer(Modifier.height(Dimensions.paddingMedium()))

        // Botones Redes Sociales
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium())
        ) {
            SocialButton(
                text = "Google",
                letter = "G",
                onClick = { viewModel.onGoogleLogin() }
            )
            SocialButton(
                text = "Facebook",
                letter = "f",
                color = PrimaryBlue,
                onClick = { viewModel.onFacebookLogin() }
            )
        }

        Spacer(Modifier.height(Dimensions.paddingLarge()))

        // Footer Registro
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = Dimensions.paddingMedium())
        ) {
            Text(
                text = "¿No tienes una cuenta? ",
                color = TextSecondary,
                fontSize = Dimensions.fontSizeMedium()
            )
            Text(
                text = "Regístrate",
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue,
                fontSize = Dimensions.fontSizeMedium(),
                modifier = Modifier.clickable { mostrarModalRoles = true }
            )
        }

        Spacer(Modifier.height(Dimensions.paddingMedium()))
    }

    // Modal Bottom Sheet para selección de rol
    if (mostrarModalRoles) {
        ModalBottomSheet(
            onDismissRequest = { mostrarModalRoles = false },
            containerColor = BackgroundWhite,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Dimensions.paddingMedium(),
                        end = Dimensions.paddingMedium(),
                        bottom = Dimensions.paddingLarge() * 2
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium())
            ) {
                Text(
                    text = "Selecciona tu perfil",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimensions.fontSizeTitle()
                    ),
                    color = TextPrimary
                )

                Text(
                    text = "¿Cómo deseas usar ContraApp?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = Dimensions.fontSizeMedium()
                    ),
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(Dimensions.paddingSmall()))

                // Botón Profesional
                Button(
                    onClick = {
                        mostrarModalRoles = false
                        onRegisterProfesionalClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.buttonHeight()),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Icon(
                        Icons.Default.Build,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(Dimensions.iconSizeMedium())
                    )
                    Spacer(Modifier.width(Dimensions.paddingSmall()))
                    Text(
                        "Registrarme como Profesional",
                        fontSize = Dimensions.fontSizeMedium(),
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botón Cliente
                OutlinedButton(
                    onClick = {
                        mostrarModalRoles = false
                        onRegisterClienteClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.buttonHeight()),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(PrimaryBlue)
                    )
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(Dimensions.iconSizeMedium())
                    )
                    Spacer(Modifier.width(Dimensions.paddingSmall()))
                    Text(
                        "Registrarme como Cliente",
                        fontSize = Dimensions.fontSizeMedium(),
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }
            }
        }
    }
}

// Componente auxiliar para botones sociales
@Composable
fun RowScope.SocialButton(
    text: String,
    letter: String,
    color: Color = Color.Black,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .weight(1f)
            .height(
                if (Dimensions.isCompact()) 48.dp
                else Dimensions.buttonHeight()
            ),
        shape = RoundedCornerShape(12.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            width = 1.dp,
            brush = androidx.compose.ui.graphics.SolidColor(DividerGray)
        )
    ) {
        Text(
            text = letter,
            fontWeight = FontWeight.Bold,
            fontSize = if (Dimensions.isCompact()) 16.sp else 18.sp,
            color = color
        )
        Spacer(Modifier.width(Dimensions.paddingSmall()))
        Text(
            text,
            fontSize = Dimensions.fontSizeSmall(),
            color = TextPrimary
        )
    }
}
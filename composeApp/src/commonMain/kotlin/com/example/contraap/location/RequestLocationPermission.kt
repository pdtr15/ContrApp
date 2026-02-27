package com.example.contraap.location

import androidx.compose.runtime.Composable

/**
 * Efecto que al ejecutarse pide el permiso de ubicación al usuario.
 * La implementación real vive en androidMain.
 *
 * @param onResult  true = permiso concedido, false = denegado
 */
@Composable
expect fun RequestLocationPermission(onResult: (Boolean) -> Unit)
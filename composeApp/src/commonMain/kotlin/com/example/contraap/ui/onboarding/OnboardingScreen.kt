package com.example.contraap.ui.onboarding

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import contraap.composeapp.generated.resources.Res
import contraap.composeapp.generated.resources.onboarding_1
import contraap.composeapp.generated.resources.onboarding_2
import contraap.composeapp.generated.resources.onboarding_3
import com.example.contraap.viewmodel.OnboardingViewModel

data class OnboardingData(
    val title: String,
    val description: String,
    val buttonText: String,
    val image: org.jetbrains.compose.resources.DrawableResource
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val pages = listOf(
        OnboardingData(
            title = "Encuentra al profesional ideal",
            description = "Busca contratistas por categoría y ubicación. Resultados rápidos y precisos.",
            buttonText = "Siguiente",
            image = Res.drawable.onboarding_1
        ),
        OnboardingData(
            title = "Contrata con seguridad",
            description = "Proyectos seguros con profesionales verificados y contratos digitales.",
            buttonText = "Siguiente",
            image = Res.drawable.onboarding_2
        ),
        OnboardingData(
            title = "Calidad garantizada",
            description = "Califica tus servicios y ayúdanos a crear una comunidad confiable.",
            buttonText = "Comienza ahora",
            image = Res.drawable.onboarding_3
        )
    )

    // Navegar cuando el onboarding se completa
    LaunchedEffect(uiState.isOnboardingCompleted) {
        if (uiState.isOnboardingCompleted) {
            onFinish()
        }
    }

    val current = pages[uiState.currentPage]

    OnboardingPage(
        title = current.title,
        description = current.description,
        buttonText = current.buttonText,
        pageIndex = uiState.currentPage,
        totalPages = uiState.totalPages,
        imageResource = current.image,
        onNext = { viewModel.onNextPage() }
    )
}
package com.example.contraap.ui.onboarding

import androidx.compose.runtime.*
import contraap.composeapp.generated.resources.Res
import contraap.composeapp.generated.resources.onboarding_1
import contraap.composeapp.generated.resources.onboarding_2
import contraap.composeapp.generated.resources.onboarding_3

data class OnboardingData(
    val title: String,
    val description: String,
    val buttonText: String,
    val image: org.jetbrains.compose.resources.DrawableResource
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var page by remember { mutableStateOf(0) }

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

    val current = pages[page]

    OnboardingPage(
        title = current.title,
        description = current.description,
        buttonText = current.buttonText,
        pageIndex = page,
        totalPages = pages.size,
        imageResource = current.image
    ) {
        if (page < pages.lastIndex) page++ else onFinish()
    }
}
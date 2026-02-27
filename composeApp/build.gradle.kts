import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {

        val commonMain by getting {
            dependencies {

                // Compose Multiplatform
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(compose.materialIconsExtended)

                // Kotlin Multiplatform libs
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

                // Supabase (Multiplatform)
                implementation("io.github.jan-tennert.supabase:postgrest-kt:2.5.1")
                implementation("io.github.jan-tennert.supabase:gotrue-kt:2.5.1")
                implementation("io.github.jan-tennert.supabase:realtime-kt:2.5.1")

                // Ktor Core (Multiplatform)
                implementation("io.ktor:ktor-client-core:2.3.7")

                implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
                implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation("androidx.core:core-ktx:1.12.0")
                implementation("io.ktor:ktor-client-android:2.3.7")
                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
                implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")
                implementation("com.google.android.gms:play-services-location:21.1.0")
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.7")
            }
        }

        val iosArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
    }
}

android {

    namespace = "com.example.contraap"

    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.contraap"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
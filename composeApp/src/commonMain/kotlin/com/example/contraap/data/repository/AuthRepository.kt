package com.example.contraap.data.repository

import com.example.contraap.data.SupabaseClient
import com.example.contraap.data.models.UserProfile
import com.example.contraap.data.models.UserRole
import com.example.contraap.data.models.ContractorProfile
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepository {
    private val supabase = SupabaseClient.client

    // Login
    suspend fun signIn(email: String, password: String): Result<UserProfile> {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val userId = supabase.auth.currentUserOrNull()?.id
                ?: return Result.failure(Exception("No se pudo obtener el ID del usuario"))

            val profile = supabase.from("profiles")
                .select(columns = Columns.ALL) {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<UserProfile>()

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Registro
    suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
        role: UserRole
    ): Result<UserProfile> {
        return try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("full_name", fullName)
                    put("role", role.name.lowercase())
                }
            }

            val userId = supabase.auth.currentUserOrNull()?.id
                ?: return Result.failure(Exception("No se pudo obtener el ID del usuario"))

            val profile = supabase.from("profiles")
                .select(columns = Columns.ALL) {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<UserProfile>()

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crear perfil de contratista
    suspend fun createContractorProfile(
        userId: String,
        categoryId: Int,
        phone: String
    ): Result<Unit> {
        return try {
            // Insertar en contractor_profiles
            supabase.from("contractor_profiles")
                .insert(
                    buildJsonObject {
                        put("id", userId)
                        put("category_id", categoryId)
                    }
                )

            // Actualizar teléfono en profiles
            supabase.from("profiles")
                .update(
                    buildJsonObject {
                        put("phone", phone)
                    }
                ) {
                    filter {
                        eq("id", userId)
                    }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Logout
    suspend fun signOut(): Result<Unit> {
        return try {
            supabase.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener usuario actual
    suspend fun getCurrentUser(): Result<UserProfile?> {
        return try {
            val userId = supabase.auth.currentUserOrNull()?.id
                ?: return Result.success(null)

            val profile = supabase.from("profiles")
                .select(columns = Columns.ALL) {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<UserProfile>()

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Verificar si hay sesión activa
    fun isUserLoggedIn(): Boolean {
        return supabase.auth.currentUserOrNull() != null
    }
}
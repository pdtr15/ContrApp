package com.example.contraap.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    @SerialName("cliente")
    CLIENTE,

    @SerialName("contratista")
    CONTRATISTA,

    @SerialName("admin")
    ADMIN
}

@Serializable
data class UserProfile(
    val id: String,
    val email: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("last_name")
    val lastName: String? = null,
    val phone: String? = null,
    val role: UserRole,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("is_verified")
    val isVerified: Boolean = false,
    @SerialName("is_active")
    val isActive: Boolean = true,
    val rating: Double = 0.0,
    @SerialName("total_jobs")
    val totalJobs: Int = 0,
    val dpi: String? = null,
    @SerialName("document_url")
    val documentUrl: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class ContractorProfile(
    val id: String,
    @SerialName("category_id")
    val categoryId: Int,
    val bio: String? = null,
    @SerialName("experience_years")
    val experienceYears: Int = 0,
    val certifications: List<String>? = null,
    val availability: Boolean = true,
    @SerialName("hourly_rate")
    val hourlyRate: Double? = null,
    @SerialName("service_radius")
    val serviceRadius: Int = 10,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
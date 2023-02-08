package com.base_ktor_project.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)

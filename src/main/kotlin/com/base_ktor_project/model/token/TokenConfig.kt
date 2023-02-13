package com.base_ktor_project.model.token

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val secret: String
)

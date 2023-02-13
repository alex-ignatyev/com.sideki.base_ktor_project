package com.base_ktor_project.model.hash

data class SaltedHash(
    val hash: String,
    val salt: String
)

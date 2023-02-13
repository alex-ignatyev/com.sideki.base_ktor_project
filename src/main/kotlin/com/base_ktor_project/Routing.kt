package com.base_ktor_project

import com.base_ktor_project.db.UsersDatabase
import com.base_ktor_project.feature.auth.configureAuthRouting
import com.base_ktor_project.model.token.TokenConfig
import com.base_ktor_project.security.password_hashing.SHA256HashingService
import com.base_ktor_project.security.token.TokenService
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting(
    hashingService: SHA256HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    userDb: UsersDatabase
) {
    routing {
        configureAuthRouting(hashingService, tokenService, tokenConfig, userDb)
    }
}

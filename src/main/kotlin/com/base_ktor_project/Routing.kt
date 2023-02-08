package com.base_ktor_project

import com.base_ktor_project.db.UsersDatabase
import com.base_ktor_project.feature.auth.configureAuthRouting
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting(
    userDb: UsersDatabase
) {
    routing {
        configureAuthRouting(userDb)
    }
}

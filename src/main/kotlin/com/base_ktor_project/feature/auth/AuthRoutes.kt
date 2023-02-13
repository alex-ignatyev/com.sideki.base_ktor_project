package com.base_ktor_project.feature.auth

import com.base_ktor_project.db.UsersDatabase
import com.base_ktor_project.feature.auth.forgot.ForgotController
import com.base_ktor_project.feature.auth.login.LoginController
import com.base_ktor_project.feature.auth.registration.RegistrationController
import com.base_ktor_project.model.token.TokenConfig
import com.base_ktor_project.security.password_hashing.SHA256HashingService
import com.base_ktor_project.security.token.TokenService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Routing.configureAuthRouting(
    hashingService: SHA256HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    userDb: UsersDatabase
) {
    post("/login") {
        LoginController(hashingService, tokenService, tokenConfig, call).performLogin(userDb)
    }

    post("/register") {
        RegistrationController(hashingService, call).performRegistration(userDb)
    }

    post("/forgot") {
        ForgotController(call).changePassword(userDb)
    }

    //В хидере Authorization должен быть токен с Bearer
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

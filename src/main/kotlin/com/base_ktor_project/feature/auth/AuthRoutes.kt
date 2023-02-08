package com.base_ktor_project.feature.auth

import com.base_ktor_project.db.UsersDatabase
import com.base_ktor_project.feature.auth.forgot.ForgotController
import com.base_ktor_project.feature.auth.login.LoginController
import com.base_ktor_project.feature.auth.registration.RegistrationController
import io.ktor.server.application.call
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post

fun Routing.configureAuthRouting(userDb: UsersDatabase) {

    post("/login") {
        LoginController(call).performLogin(userDb)
    }

    post("/register") {
        RegistrationController(call).performRegistration(userDb)
    }

    post("/forgot") {
        ForgotController(call).changePassword(userDb)
    }
}

package com.base_ktor_project.feature.auth.login

import com.base_ktor_project.db.UsersDatabase
import com.base_ktor_project.model.LoginRequest
import com.base_ktor_project.model.TokenResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveOrNull
import io.ktor.server.response.respond
import java.util.UUID

class LoginController(private val call: ApplicationCall) {

    suspend fun performLogin(userDb: UsersDatabase) {
        val receive = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return
        }

        val user = userDb.getUserByUsername(receive.login) ?: kotlin.run {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return
        }

        val isValidPassword = receive.password == user.password
        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return
        }


        val token = UUID.randomUUID().toString()
        call.respond(HttpStatusCode.OK, TokenResponse(token))
    }
}

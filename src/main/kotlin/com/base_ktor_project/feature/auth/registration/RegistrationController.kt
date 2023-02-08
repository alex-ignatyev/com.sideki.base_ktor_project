package com.base_ktor_project.feature.auth.registration

import com.base_ktor_project.db.UsersDatabase
import com.base_ktor_project.model.LoginRequest
import com.base_ktor_project.model.TokenResponse
import com.base_ktor_project.model.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveOrNull
import io.ktor.server.response.respond
import java.util.UUID

class RegistrationController(private val call: ApplicationCall) {

    suspend fun performRegistration(userDb: UsersDatabase) {
        val receive = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return
        }
        val areFieldsBlank = receive.login.isBlank() || receive.password.isBlank()
        val isPwTooShort = receive.password.length < 6
        if (areFieldsBlank || isPwTooShort) {
            call.respond(HttpStatusCode.Conflict, "Password is too short")
            return
        }

        val user = User(
            username = receive.login,
            password = receive.password
        )

        val wasAcknowledged = userDb.insertUser(user)

        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return
        }

        call.respond(HttpStatusCode.OK)
    }
}

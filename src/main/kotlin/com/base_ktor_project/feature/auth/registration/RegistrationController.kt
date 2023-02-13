package com.base_ktor_project.feature.auth.registration

import com.base_ktor_project.db.UsersDatabase
import com.base_ktor_project.model.LoginRequest
import com.base_ktor_project.model.User
import com.base_ktor_project.security.password_hashing.SHA256HashingService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveOrNull
import io.ktor.server.response.respond

class RegistrationController(
    private val hashingService: SHA256HashingService,
    private val call: ApplicationCall
) {

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

        if (createUser(userDb, receive)) {
            call.respond(HttpStatusCode.OK)
            return
        }

        call.respond(HttpStatusCode.Conflict)
    }

    private suspend fun createUser(userDb: UsersDatabase, receive: LoginRequest): Boolean {
        val saltedHashPassword = hashingService.generateSaltedHash(receive.password)
        val user = User(
            username = receive.login,
            password = saltedHashPassword.hash,
            salt = saltedHashPassword.salt
        )

        return userDb.insertUser(user)
    }
}

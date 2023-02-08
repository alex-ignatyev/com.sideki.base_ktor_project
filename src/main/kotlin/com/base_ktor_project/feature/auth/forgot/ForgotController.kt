package com.base_ktor_project.feature.auth.forgot

import com.base_ktor_project.db.UsersDatabase
import com.base_ktor_project.model.LoginRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveOrNull
import io.ktor.server.response.respond

class ForgotController(private val call: ApplicationCall) {

    suspend fun changePassword(userDb: UsersDatabase) {
        val receive = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return
        }

        val user = userDb.getUserByUsername(receive.login) ?: kotlin.run {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return
        }

        val isUserWasUpdated = userDb.updateUserPassword(user, receive.password)
        if (!isUserWasUpdated) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return
        }
        call.respond(HttpStatusCode.OK)
    }
}

package com.base_ktor_project.feature.auth.login

import com.base_ktor_project.db.UsersDatabase
import com.base_ktor_project.model.LoginRequest
import com.base_ktor_project.model.TokenResponse
import com.base_ktor_project.model.hash.SaltedHash
import com.base_ktor_project.model.token.TokenClaim
import com.base_ktor_project.model.token.TokenConfig
import com.base_ktor_project.security.password_hashing.HashingService
import com.base_ktor_project.security.token.TokenService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveOrNull
import io.ktor.server.response.respond

class LoginController(
    private val hashingService: HashingService,
    private val tokenService: TokenService,
    private val tokenConfig: TokenConfig,
    private val call: ApplicationCall
) {

    suspend fun performLogin(userDb: UsersDatabase) {
        val receive = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return
        }

        val user = userDb.getUserByUsername(receive.login) ?: kotlin.run {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return
        }

        val isValidPassword = hashingService.verify(
            value = receive.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )

        call.respond(HttpStatusCode.OK, TokenResponse(token))
    }
}

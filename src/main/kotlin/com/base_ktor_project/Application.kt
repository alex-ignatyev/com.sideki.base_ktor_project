package com.base_ktor_project

import com.base_ktor_project.db.UsersDatabase
import com.base_ktor_project.plugins.configureMonitoring
import com.base_ktor_project.plugins.configureSerialization
import io.ktor.server.application.Application
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSerialization()
    configureMonitoring()

    //val mongoPw = System.getenv("MONGO_PW") // Походу это кей для закрытой БД который подставляется в УРЛ
    val db = KMongo.createClient(
        //connectionString = "mongodb+srv://philipplackner:$mongoPw@cluster0.mvi9z.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase("app_db")
    val usersDb = UsersDatabase(db)

    /* val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()
    configureSecurity(tokenConfig) */

    configureRouting(usersDb)
}

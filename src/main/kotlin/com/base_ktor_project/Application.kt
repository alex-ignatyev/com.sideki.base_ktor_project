package com.base_ktor_project

import com.base_ktor_project.db.UsersDatabase
import com.base_ktor_project.model.token.TokenConfig
import com.base_ktor_project.plugins.configureMonitoring
import com.base_ktor_project.plugins.configureSecurity
import com.base_ktor_project.plugins.configureSerialization
import com.base_ktor_project.security.password_hashing.SHA256HashingService
import com.base_ktor_project.security.token.JwtTokenService
import io.ktor.server.application.Application
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

//FIXME Добавить DI
//FIXME Отрефачить весь код
//FIXME Добавить запросы тестовые в приложение с картинками и тд, все запросы с токеном

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSerialization()
    configureMonitoring()

    //val mongoPw = System.getenv("MONGO_PW") // Пароль для закрытой БД который подставляется в УРЛ и хранится в Edit Configuration -> Environment Variables
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://test:test@kmmtestapp.x6lwixa.mongodb.net/?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase("kmmtestapp_db")
    val usersDb = UsersDatabase(db)

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L, // 1 Year in miliseconds
        secret = "JWT_SECRET" // Нужно хранить в в Edit Configuration -> Environment Variables
    )
    val hashingService = SHA256HashingService()

    configureSecurity(tokenConfig)
    configureRouting(hashingService, tokenService, tokenConfig, usersDb)
}

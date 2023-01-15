package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting(){
    authenticate("auth-jwt") {

        get("/user"){
            val principal = call.principal<JWTPrincipal>()
            val usrId = principal!!.payload.getClaim("uid").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText("Hello, $usrId! Token is expired at $expiresAt ms.")
        }
    }
}
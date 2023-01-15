package com.example.routes

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.*
import com.toxicbakery.bcrypt.Bcrypt
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*
import java.util.UUID.randomUUID

fun Route.authorizationRouting(){
    val secret = environment?.config?.property("jwt.secret")?.getString()
    val audience = environment?.config?.property("jwt.audience")?.getString()
    val issuer = environment?.config?.property("jwt.issuer")?.getString()
    post("/signup"){
        val signup = call.receive<Signup>()
        val user = userStorage.find { it.email == signup.email }
        if(user != null){
            return@post call.respondText("user already exist", status = HttpStatusCode.BadRequest)
        }
        val uuid: String = randomUUID().toString()

        val passwordHash: String = Bcrypt.hash(signup.password, BCrypt.SALT_LENGTH).toString()
        userStorage.add(User(uuid, signup.name, signup.email, passwordHash, Date(System.currentTimeMillis()).toString(), Date(System.currentTimeMillis()).toString()))
        val accessToken = JWT.create()
            .withAudience(audience)
            .withClaim("uid", uuid)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(secret))

        val refreshToken = JWT.create()
            .withAudience(audience)
            .withClaim("uid", uuid)
            .withIssuer(issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(secret))
        call.respond(HttpStatusCode.Created,Response(HttpStatusCode.Created.value, ResponseData("success", "user Created", accessToken, refreshToken)))
    }

    post("/login"){
        val login = call.receive<Login>()

        val user: User = userStorage.find { it.email == login.email } ?: return@post call.respondText("user does not exists !", status = HttpStatusCode.NotFound)

        val accessToken = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("uid", user.id)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(secret))

        val refreshToken = JWT.create()
            .withAudience(audience)
            .withClaim("uid", user.id)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(secret))

        call.respond(HttpStatusCode.Accepted,Response(HttpStatusCode.Accepted.value, ResponseData("success", "user Created", accessToken, refreshToken)))
    }
}
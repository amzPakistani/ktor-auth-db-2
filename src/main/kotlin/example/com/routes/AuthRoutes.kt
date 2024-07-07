package example.com.routes

import example.com.data.request.AuthRequest
import example.com.data.response.AuthResponse
import example.com.data.user.UserDataSource
import example.com.data.user.model.User
import example.com.security.hashing.HashingService
import example.com.security.hashing.SaltedHash
import example.com.security.token.TokenClaim
import example.com.security.token.TokenConfig
import example.com.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signup(
    userDataSource: UserDataSource,
    hashingService: HashingService
) {
    post("signup") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val isBlank = request.username.isBlank() || request.password.isBlank()
        val shortPW = request.password.length < 8
        if (isBlank || shortPW) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }
        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val wasAcknowledged = userDataSource.insertUser(user)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.login(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("login") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val user = userDataSource.getUserByUsername(request.username)

        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "Incorrect Username or Password")
            return@post
        }

        val isPWValid =
            hashingService.verify(
                request.password,
                saltedHash = SaltedHash(
                    hash = user.password,
                    salt = user.salt
                )
            )

        if (!isPWValid) {
            call.respond(HttpStatusCode.Conflict, "Incorrect Username or Password")
            return@post
        }

        val token = tokenService.generate(
            tokenConfig,
            TokenClaim("userId", user.id.toString())
        )

        call.respond(
            HttpStatusCode.OK, message = AuthResponse(
                token
            )
        )
    }
}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo(){
    authenticate{
        get("secret"){
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
        }
    }
}
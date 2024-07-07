package example.com.plugins

import example.com.data.user.UserDataSource
import example.com.routes.authenticate
import example.com.routes.getSecretInfo
import example.com.routes.login
import example.com.routes.signup
import example.com.security.hashing.HashingService
import example.com.security.token.TokenConfig
import example.com.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(userDataSource: UserDataSource, hashingService: HashingService, tokenSevice: TokenService, tokenConfig: TokenConfig) {
    routing {
        login(userDataSource,hashingService,tokenSevice,tokenConfig)
        signup( userDataSource, hashingService)
        authenticate()
        getSecretInfo()
    }
}

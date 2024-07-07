package example.com

import com.mongodb.kotlin.client.coroutine.MongoClient
import example.com.data.user.MongoUserDataSource
import example.com.plugins.*
import example.com.security.hashing.SHA256HashingService
import example.com.security.token.JWTTokenService
import example.com.security.token.TokenConfig
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val dbName = "ktor-prac"
    val dbPW = System.getenv("MONGO_PW")
    val db = MongoClient.create(
        connectionString = "mongodb+srv://abdulmajidzeeshan4:$dbPW@cluster0.zgl8kvx.mongodb.net/$dbName?retryWrites=true&w=majority&appName=Cluster0"
    ).getDatabase(dbName)

    val userDataSource = MongoUserDataSource(db)

    val tokenService = JWTTokenService()

    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiry = 1000 * 60 * 60 *24*365,
        secret = System.getenv("SECRET")
    )

    val hashingService = SHA256HashingService()
    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig)
    configureRouting(userDataSource = userDataSource, hashingService, tokenConfig = tokenConfig, tokenSevice = tokenService )
}

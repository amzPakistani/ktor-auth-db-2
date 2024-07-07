package example.com

import com.mongodb.kotlin.client.coroutine.MongoClient
import example.com.data.MongoUserDataSource
import example.com.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val dbName = "ktor-prac"
    val db = MongoClient.create(
        connectionString = "mongodb+srv://abdulmajidzeeshan4:tutorial202@cluster0.zgl8kvx.mongodb.net/$dbName?retryWrites=true&w=majority&appName=Cluster0"
    ).getDatabase(dbName)

    val userDataSource = MongoUserDataSource(db)

    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}

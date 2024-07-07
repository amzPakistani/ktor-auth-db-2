package example.com.data.user

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import example.com.data.user.model.User
import kotlinx.coroutines.flow.firstOrNull

class MongoUserDataSource(db: MongoDatabase) : UserDataSource {

    private val users = db.getCollection<User>("users", User::class.java)

    override suspend fun getUserByUsername(username: String): User? {
        return users.find(Filters.eq("username", username)).firstOrNull()
    }

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }
}


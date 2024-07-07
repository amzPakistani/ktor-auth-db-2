package example.com.data

import example.com.data.model.User

interface UserDataSource {
    suspend fun getUserByUsername(username:String): User?
    suspend fun insertUser(user: User): Boolean
}
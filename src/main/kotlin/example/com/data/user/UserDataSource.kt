package example.com.data.user

import example.com.data.user.model.User

interface UserDataSource {
    suspend fun getUserByUsername(username:String): User?
    suspend fun insertUser(user: User): Boolean
}
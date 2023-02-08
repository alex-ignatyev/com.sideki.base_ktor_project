package com.base_ktor_project.db

import com.base_ktor_project.model.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UsersDatabase(
    private val db: CoroutineDatabase
) {

    private val users = db.getCollection<User>()

    suspend fun insertUser(user: User): Boolean {
        val existedUser = users.findOne(User::username eq user.username)
        return if (existedUser == null) {
            users.insertOne(user).wasAcknowledged()
        } else {
            false
        }
    }

    suspend fun updateUserPassword(user: User, newPassword: String): Boolean {
        val existedUser = users.findOne(User::username eq user.username)
        return if (existedUser == null) {
            false
        } else {
            val updatedUser = existedUser.copy(password = newPassword)
            users.updateOneById(user.username, updatedUser, updateOnlyNotNullProperties = true).wasAcknowledged()
        }
    }

    suspend fun getUserByUsername(username: String): User? {
        return users.findOne(User::username eq username)
    }
}

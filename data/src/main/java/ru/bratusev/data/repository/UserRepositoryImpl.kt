package ru.bratusev.data.repository

import com.google.gson.Gson
import ru.bratusev.data.storage.local.UserStorage
import ru.bratusev.domain.model.UserData
import ru.bratusev.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userStorage: UserStorage,
    private val gson: Gson,
) : UserRepository {
    override fun getUserData(): UserData? {
        val user = userStorage.getUser()
        return if (user.isEmpty()) {
            null
        } else {
            gson.fromJson(user, UserData::class.java)
        }
    }

    override fun saveUserData(userData: UserData) {
        userStorage.saveUser(gson.toJson(userData))
    }
}

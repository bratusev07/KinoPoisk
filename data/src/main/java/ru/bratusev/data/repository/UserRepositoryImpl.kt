package ru.bratusev.data.repository

import ru.bratusev.data.model.UserDTO
import ru.bratusev.data.model.toUserData
import ru.bratusev.data.storage.local.UserStorage
import ru.bratusev.domain.model.UserData
import ru.bratusev.domain.repository.UserRepository

class UserRepositoryImpl(private val userStorage: UserStorage) : UserRepository {

    override fun getUserData(): UserData {
        return userStorage.getUser().toUserData()
    }

    override fun saveUserData(userData: UserData) {
        userStorage.saveUser(UserDTO(login = userData.login, password = userData.password))
    }
}
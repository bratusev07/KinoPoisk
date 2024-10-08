package ru.bratusev.domain.repository

import ru.bratusev.domain.model.UserData

interface UserRepository {
    fun getUserData(): UserData

    fun saveUserData(userData: UserData)
}

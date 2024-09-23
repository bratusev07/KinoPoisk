package ru.bratusev.data.storage.local

import ru.bratusev.data.model.UserDTO

interface UserStorage {

    fun saveUser(userDTO: UserDTO)

    fun getUser(): UserDTO
}
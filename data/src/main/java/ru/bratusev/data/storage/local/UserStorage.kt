package ru.bratusev.data.storage.local

interface UserStorage {
    fun saveUser(user: String)

    fun getUser(): String
}

package ru.bratusev.data.storage.local

import android.content.Context
import ru.bratusev.data.model.UserDTO


private const val SHARED_PREFS_NAME = "shared_prefs_name"
private const val KEY_LOGIN = "login"
private const val KEY_PASSWORD = "password"
private const val DEFAULT_LOGIN = ""
private const val DEFAULT_PASSWORD = ""

class UserStorageImpl(context: Context) : UserStorage {

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveUser(userDTO: UserDTO) {
        sharedPreferences.edit().putString(KEY_LOGIN, userDTO.login).apply()
        sharedPreferences.edit().putString(KEY_PASSWORD, userDTO.password).apply()
    }

    override fun getUser(): UserDTO {
        val login = sharedPreferences.getString(KEY_LOGIN, DEFAULT_LOGIN)
        val password = sharedPreferences.getString(KEY_PASSWORD, DEFAULT_PASSWORD)
        return UserDTO(login = login, password = password)
    }
}
package ru.bratusev.data.storage.local

import android.content.Context

private const val SHARED_PREFS_NAME = "shared_prefs_name"
private const val KEY_USER = "user"
private const val DEFAULT_USER = ""

class UserStorageImpl(
    context: Context,
) : UserStorage {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveUser(user: String) {
        sharedPreferences.edit().putString(KEY_USER, user).apply()
    }

    override fun getUser(): String = sharedPreferences.getString(KEY_USER, DEFAULT_USER) ?: DEFAULT_USER
}

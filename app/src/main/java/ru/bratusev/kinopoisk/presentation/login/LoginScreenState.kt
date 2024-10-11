package ru.bratusev.kinopoisk.presentation.login

data class LoginScreenState(
    val loginState: Boolean = false,
    val lastLoginTime: String = "",
)

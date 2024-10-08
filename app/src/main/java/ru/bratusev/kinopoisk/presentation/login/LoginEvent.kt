package ru.bratusev.kinopoisk.presentation.login

sealed class LoginEvent {
    data class OnClickLogin(val login: String, val password: String) : LoginEvent()
}
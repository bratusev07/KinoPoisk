package ru.bratusev.kinopoisk.presentation.login

sealed class LoginLabel {
    data object GoToNext : LoginLabel()

    data object OnBackClick : LoginLabel()

    data class ShowPasswordAlert(
        val message: String,
    ) : LoginLabel()
}

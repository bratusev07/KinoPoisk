package ru.bratusev.kinopoisk.presentation.details

sealed class DetailLabel {
    data object GoToPrevious : DetailLabel()

    data class OpenUrl(
        val webUrl: String,
    ) : DetailLabel()
}

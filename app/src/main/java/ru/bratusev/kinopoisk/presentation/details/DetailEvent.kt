package ru.bratusev.kinopoisk.presentation.details

sealed class DetailEvent {
    data class OnClickWebUrl(
        val webUrl: String,
    ) : DetailEvent()

    data object OnClickBack : DetailEvent()

    data class OnFragmentStart(
        val kinopoiskId: Int,
    ) : DetailEvent()
}

package ru.bratusev.kinopoisk.presentation.search

import ru.bratusev.kinopoisk.presentation.items.FilmArgs

sealed class SearchLabel {
    data object GoToPrevious : SearchLabel()

    data class GoToNext(
        val film: FilmArgs,
    ) : SearchLabel()

    data class ShowDatePicker(
        val isStart: Boolean,
    ) : SearchLabel()

    data class ShowToast(
        val message: String,
    ) : SearchLabel()
}

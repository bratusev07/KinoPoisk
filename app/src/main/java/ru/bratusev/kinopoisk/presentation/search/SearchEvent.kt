package ru.bratusev.kinopoisk.presentation.search

sealed class SearchEvent {
    data class OnClickFilmItem(
        val filmId: String,
    ) : SearchEvent()

    data object OnClickBack : SearchEvent()

    data class OnClickYearPicker(
        val isStart: Boolean,
    ) : SearchEvent()

    data class OnScrollDown(
        val inputSearch: String,
    ) : SearchEvent()

    data object OnRefresh : SearchEvent()

    data object OnSort : SearchEvent()

    data class OnSearchFilms(
        val inputSearch: String,
    ) : SearchEvent()

    data object OnFragmentStart : SearchEvent()

    data class OnYearSelected(
        val selectedYear: String,
        val isStart: Boolean,
    ) : SearchEvent()
}

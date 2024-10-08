package ru.bratusev.kinopoisk.presentation.search

import android.os.Bundle

sealed class SearchLabel {
    data object GoToPrevious : SearchLabel()

    data class GoToNext(
        val bundle: Bundle,
    ) : SearchLabel()

    data class ShowDatePicker(
        val isStart: Boolean,
    ) : SearchLabel()

    data class ShowToast(
        val message: String,
    ) : SearchLabel()
}

package ru.bratusev.kinopoisk.presentation.search

import ru.bratusev.domain.model.Film

sealed class ListItem {
    data class DefaultItem(val film: Film) : ListItem()
    data class YearItem(val year: String) : ListItem()
}
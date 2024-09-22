package ru.bratusev.kinopoisk.presentation.search

import ru.bratusev.domain.model.Film

interface OnItemClickListener {
    fun onItemClick(film: Film)
}

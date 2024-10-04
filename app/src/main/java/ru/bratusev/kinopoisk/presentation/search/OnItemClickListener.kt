package ru.bratusev.kinopoisk.presentation.search

import ru.bratusev.kinopoisk.presentation.items.FilmItemUI


interface OnItemClickListener {
    fun onItemClick(film: FilmItemUI)
}

package ru.bratusev.kinopoisk.presentation.details

import ru.bratusev.domain.model.FilmDetail
import ru.bratusev.kinopoisk.presentation.items.BaseItem

data class DetailScreenState(
    val filmDetail: FilmDetail = FilmDetail("", "", "", ""),
    val frameList: List<BaseItem> = listOf(),
    var webUrl: String = "",
)

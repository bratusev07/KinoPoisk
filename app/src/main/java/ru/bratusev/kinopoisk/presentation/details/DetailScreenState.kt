package ru.bratusev.kinopoisk.presentation.details

import ru.bratusev.domain.model.FilmDetail
import ru.bratusev.kinopoisk.presentation.items.BaseItem
import ru.bratusev.kinopoisk.presentation.items.FilmArgs

data class DetailScreenState(
    val filmDetail: FilmDetail = FilmDetail("", "", "", ""),
    val frameList: List<BaseItem> = emptyList(),
    var webUrl: String = "",
    var film: FilmArgs = FilmArgs(),
)

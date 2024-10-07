package ru.bratusev.kinopoisk.presentation.search

import ru.bratusev.domain.model.FilmRequestParams
import ru.bratusev.kinopoisk.presentation.items.BaseItem

data class SearchScreenState(
    val filmList: List<BaseItem> = ArrayList(),
    val isLoading: Boolean = false,
    val params: FilmRequestParams = FilmRequestParams(),
    var rotationSortIcon: Float = 0f,
    var isRefreshing: Boolean = false,
)
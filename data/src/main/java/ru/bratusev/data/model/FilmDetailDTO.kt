package ru.bratusev.data.model

import ru.bratusev.domain.model.FilmDetail

data class FilmDetailDTO(
    val description: String?,
    val webUrl: String?,
    val startYear: String?,
    val endYear: String?,
)

fun FilmDetailDTO.toFilmDetail(): FilmDetail {
    return FilmDetail(
        description = description ?: "нет данных",
        webUrl = webUrl ?: "нет данных",
        startYear = startYear ?: "нет данных",
        endYear = endYear ?: "нет данных"
    )
}
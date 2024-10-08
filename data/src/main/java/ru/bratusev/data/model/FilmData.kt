package ru.bratusev.data.model

data class FilmData(
    private val total: Int,
    private val totalPages: Int,
    val items: List<FilmDTO>,
)

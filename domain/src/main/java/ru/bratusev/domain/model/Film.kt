package ru.bratusev.domain.model

data class Film(
    val kinopoiskId: Int,
    val name: String? = "Default name",
    val country: String,
    val genre: String,
    val ratingKinopoisk: Float,
    val year: Int,
    val posterUrl: String,
    val posterUrlPreview: String,
    val frameList: List<String>
)
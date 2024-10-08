package ru.bratusev.data.model

data class FilmRequestParams(
    var order: String = "RATING",
    var year: String = "1000",
    var page: Int = 1,
    var endYear: String = "2024",
)

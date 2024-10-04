package ru.bratusev.kinopoisk.presentation.search


sealed class BaseItem(val itemId: String) {

    data class FilmItemUI(
        val kinopoiskId: Int,
        val name: String? = "Default name",
        val country: String,
        val genre: String,
        val ratingKinopoisk: Float,
        val year: Int,
        val posterUrl: String,
        val posterUrlPreview: String,
        val frameList: ArrayList<String>
    ) : BaseItem(kinopoiskId.toString())

    data class YearItemUI(val year: String) : BaseItem(year)
}
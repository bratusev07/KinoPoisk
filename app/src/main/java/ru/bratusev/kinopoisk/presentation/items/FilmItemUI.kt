package ru.bratusev.kinopoisk.presentation.items

data class FilmItemUI(
    override val itemId: String,
    val name: String? = "Default name",
    val country: String,
    val genre: String,
    val ratingKinopoisk: Float,
    val year: Int,
    val posterUrl: String,
    val posterUrlPreview: String,
    val frameList: ArrayList<String>
) : BaseItem
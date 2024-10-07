package ru.bratusev.kinopoisk.presentation.items

data class FilmItemUI(
    override val itemId: String,
    val name: String? = "Default name",
    val date: String,
    val genre: String,
    val ratingKinopoisk: Float,
    val posterUrl: String,
    val posterUrlPreview: String,
    val frameList: List<String>
) : BaseItem
package ru.bratusev.kinopoisk.presentation.items

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilmItemUI(
    override val itemId: String,
    val name: String? = "Default name",
    val date: String = "",
    val genre: String = "",
    val ratingKinopoisk: Float = 0f,
    val posterUrl: String = "",
    val posterUrlPreview: String = "",
    val frameList: List<String> = emptyList(),
) : BaseItem,
    Parcelable

fun FilmItemUI.toFilmArgs(): FilmArgs = FilmArgs(itemId.toInt(), name, date, genre, ratingKinopoisk, posterUrlPreview, frameList)

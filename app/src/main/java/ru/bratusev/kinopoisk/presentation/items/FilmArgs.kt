package ru.bratusev.kinopoisk.presentation.items

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilmArgs(
    val kinopoiskId: Int = 0,
    val name: String? = "Default name",
    val country: String = "",
    val genre: String = "",
    val ratingKinopoisk: Float = 0f,
    val posterUrlPreview: String = "",
    val frameList: List<String> = emptyList(),
) : Parcelable

package ru.bratusev.data.model

import ru.bratusev.domain.model.Film

data class FilmDTO(
    val kinopoiskId: Int?,
    val nameRu: String?,
    val nameEn: String?,
    val nameOriginal: String?,
    val countries: ArrayList<Country>?,
    val genres: ArrayList<Genre>?,
    val ratingKinopoisk: Float?,
    val year: Int?,
    val posterUrl: String?,
    val posterUrlPreview: String?
) {

    fun getGenres(): String {
        return genres?.joinToString(", ") ?: "нет данных"
    }

    fun getCountries(): String {
        return countries?.joinToString(", ") ?: "нет данных"
    }

    fun getName(): String {
        return if (!nameOriginal.isNullOrEmpty()) {
            nameOriginal
        } else if (!nameEn.isNullOrEmpty()) {
            nameEn
        } else {
            nameRu ?: "нет данных"
        }
    }

}

data class Genre(internal val genre: String) {
    override fun toString(): String {
        return genre
    }
}

data class Country(internal val country: String) {
    override fun toString(): String {
        return country
    }
}

fun FilmDTO.toFilm(): Film {
    return Film(
        kinopoiskId = kinopoiskId ?: 0,
        name = getName(),
        country = getCountries(),
        genre = getGenres(),
        ratingKinopoisk = ratingKinopoisk ?: 0.0f,
        year = year ?: 0,
        posterUrl = posterUrl ?: "нет данных",
        posterUrlPreview = posterUrlPreview ?: "нет данных",
        frameList = arrayListOf()
    )
}
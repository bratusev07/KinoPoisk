package ru.bratusev.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.bratusev.domain.model.Film

@Entity(tableName = "films")
data class FilmEntity(
    @PrimaryKey val kinopoiskId: Int,
    val name: String,
    @ColumnInfo(defaultValue = "нет данных") val description: String?,
    @ColumnInfo(defaultValue = "нет данных") val webUrl: String?,
    val countries: String,
    val genres: String,
    val ratingKinopoisk: Float,
    val releaseYear: String,
    @ColumnInfo(defaultValue = "нет данных") val startYear: String?,
    @ColumnInfo(defaultValue = "нет данных") val endYear: String?,
    val posterUrlPreview: String
)

fun FilmEntity.toFilm(): Film {
    return Film(
        kinopoiskId = kinopoiskId,
        name = name,
        country = countries,
        genre = genres,
        ratingKinopoisk = ratingKinopoisk,
        year = releaseYear.toInt(),
        posterUrl = posterUrlPreview,
        posterUrlPreview = posterUrlPreview,
        frameList = arrayListOf()
    )
}
package ru.bratusev.kinopoisk.presentation.mapper

import ru.bratusev.domain.model.Film
import ru.bratusev.kinopoisk.presentation.items.BaseItem
import ru.bratusev.kinopoisk.presentation.items.FilmItemUI
import ru.bratusev.kinopoisk.presentation.items.YearItemUI

class SearchScreenMapper : Mapper<List<Film>, List<BaseItem>> {

    override fun transform(data: List<Film>): List<BaseItem> {
        val filmList = mutableListOf<BaseItem>()
        val seenYears = mutableSetOf<Int>()
        data.forEach { film ->
            val year = film.year
            if (year !in seenYears) {
                filmList.add(mapToYearUI(year.toString()))
                seenYears.add(year)
            }
            filmList.add(mapToFilmUI(film))
        }
        return filmList
    }

    private fun mapToFilmUI(film: Film): FilmItemUI = FilmItemUI(
        film.kinopoiskId.toString(),
        film.name,
        film.country,
        film.genre,
        film.ratingKinopoisk,
        film.year,
        film.posterUrl,
        film.posterUrlPreview,
        film.frameList
    )

    private fun mapToYearUI(year: String): YearItemUI = YearItemUI(year)

}
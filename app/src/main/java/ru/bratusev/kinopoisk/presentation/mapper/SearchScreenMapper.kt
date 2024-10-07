package ru.bratusev.kinopoisk.presentation.mapper

import ru.bratusev.domain.model.Film
import ru.bratusev.kinopoisk.presentation.items.BaseItem
import ru.bratusev.kinopoisk.presentation.items.FilmItemUI
import ru.bratusev.kinopoisk.presentation.items.YearItemUI

class SearchScreenMapper : Mapper<List<Film>, List<BaseItem>> {

    override fun transform(data: List<Film>): List<BaseItem> {
        val seenYears = mutableSetOf<Int>()
        return buildList {
            data.forEach { film ->
                val year = film.year
                if (year !in seenYears) {
                    add(mapToYearUI(year.toString()))
                    seenYears.add(year)
                }
                add(mapToFilmUI(film))
            }
        }
    }

    private fun mapToFilmUI(film: Film): FilmItemUI = FilmItemUI(
        film.kinopoiskId.toString(),
        film.name,
        "${film.year}, ${film.country}",
        film.genre,
        film.ratingKinopoisk,
        film.posterUrl,
        film.posterUrlPreview,
        film.frameList
    )

    private fun mapToYearUI(year: String): YearItemUI = YearItemUI(year, year)

}
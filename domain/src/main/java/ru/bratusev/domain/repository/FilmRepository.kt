package ru.bratusev.domain.repository

import ru.bratusev.domain.model.Film
import ru.bratusev.domain.model.FilmDetail
import ru.bratusev.domain.model.Frame

interface FilmRepository {

    suspend fun getFilms(order: String, year: String, page: Int): ArrayList<Film>

    suspend fun getFilmById(kinopoiskId: Int): FilmDetail

    suspend fun getFilmFrames(kinopoiskId: Int): ArrayList<Frame>

    suspend fun insertFilmIntoDB(filmEntity: Film): Boolean

    suspend fun getFilmsFromDB(): ArrayList<Film>

    suspend fun getFilmByKeyword(keyword: String): ArrayList<Film>
}
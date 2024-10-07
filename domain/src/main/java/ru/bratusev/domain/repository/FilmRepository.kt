package ru.bratusev.domain.repository

import ru.bratusev.domain.model.Film
import ru.bratusev.domain.model.FilmDetail
import ru.bratusev.domain.model.FilmRequestParams
import ru.bratusev.domain.model.Frame

interface FilmRepository {

    suspend fun getFilms(params: FilmRequestParams): List<Film>

    suspend fun getFilmById(kinopoiskId: Int): FilmDetail

    suspend fun getFilmFrames(kinopoiskId: Int): List<Frame>

    suspend fun insertFilmIntoDB(filmEntity: Film): Boolean

    suspend fun getFilmsFromDB(): List<Film>

    suspend fun getFilmByKeyword(keyword: String): List<Film>
}
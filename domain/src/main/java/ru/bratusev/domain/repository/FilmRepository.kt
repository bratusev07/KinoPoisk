package ru.bratusev.domain.repository

import ru.bratusev.domain.model.Film
import ru.bratusev.domain.model.FilmDetail
import ru.bratusev.domain.model.Frame

interface FilmRepository {

    suspend fun getFilms(): ArrayList<Film>

    suspend fun getFilmById(kinopoiskId: Int): FilmDetail

    //suspend fun getFilmByKeyword(keyword: String): Film

    suspend fun getFilmFrames(kinopoiskId: Int): ArrayList<Frame>

}
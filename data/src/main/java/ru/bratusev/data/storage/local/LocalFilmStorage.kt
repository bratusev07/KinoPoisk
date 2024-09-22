package ru.bratusev.data.storage.local

import ru.bratusev.data.model.FilmDTO

interface LocalFilmStorage {

    suspend fun getFilms(): ArrayList<FilmDTO>

    suspend fun getFilmById(kinopiskId: Int): FilmDTO
}
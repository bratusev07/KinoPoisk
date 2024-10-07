package ru.bratusev.data.storage.local

import ru.bratusev.data.model.FilmEntity

interface LocalFilmStorage {

    suspend fun insertFilmIntoDB(filmEntity: FilmEntity): Boolean

    suspend fun getFilmsFromDB(): List<FilmEntity>

    suspend fun getFilmByKeyword(keyword: String): List<FilmEntity>
}
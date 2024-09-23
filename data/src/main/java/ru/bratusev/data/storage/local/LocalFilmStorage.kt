package ru.bratusev.data.storage.local

import ru.bratusev.data.model.FilmEntity

interface LocalFilmStorage {

    suspend fun insertFilmIntoDB(filmEntity: FilmEntity): Boolean

    suspend fun getFilmsFromDB(): ArrayList<FilmEntity>

    suspend fun getFilmByKeyword(keyword: String): ArrayList<FilmEntity>
}
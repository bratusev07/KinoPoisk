package ru.bratusev.data.storage.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.bratusev.data.model.FilmEntity
import ru.bratusev.data.storage.local.db.FilmDataBase

class FilmStorageImpl(private val filmDB: FilmDataBase): LocalFilmStorage {

    override suspend fun insertFilmIntoDB(filmEntity: FilmEntity): Boolean {
        withContext(Dispatchers.IO){
            filmDB.getFilmDao().insertFilm(filmEntity)
        }
        return true
    }

    override suspend fun getFilmsFromDB(): List<FilmEntity> {
        return withContext(Dispatchers.IO){
            filmDB.getFilmDao().getFilms()
        }
    }

    override suspend fun getFilmByKeyword(keyword: String): List<FilmEntity> {
        return withContext(Dispatchers.IO){
            filmDB.getFilmDao().getFilmsByKeyword(keyword)
        }
    }

}
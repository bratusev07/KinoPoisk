package ru.bratusev.data.storage.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.bratusev.data.model.FilmEntity

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = FilmEntity::class)
    fun insertFilm(film: FilmEntity)

    @Query("SELECT * FROM films")
    fun getFilms(): List<FilmEntity>

    @Query("SELECT * FROM films WHERE name LIKE '%' || :keyword || '%' OR" +
            " genres LIKE '%' || :keyword || '%' OR " +
            "countries LIKE '%' || :keyword || '%'")
    fun getFilmsByKeyword(keyword: String): List<FilmEntity>

}
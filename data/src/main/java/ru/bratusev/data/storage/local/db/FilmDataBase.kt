package ru.bratusev.data.storage.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.bratusev.data.model.FilmEntity

@Database(version = 1, entities = [FilmEntity::class])
abstract class FilmDataBase : RoomDatabase() {
    abstract fun getFilmDao(): FilmDao
}

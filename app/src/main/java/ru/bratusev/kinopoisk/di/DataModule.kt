package ru.bratusev.kinopoisk.di

import androidx.room.Room
import org.koin.dsl.module
import ru.bratusev.data.repository.FilmRepositoryImpl
import ru.bratusev.data.repository.UserRepositoryImpl
import ru.bratusev.data.storage.local.FilmStorageImpl
import ru.bratusev.data.storage.local.LocalFilmStorage
import ru.bratusev.data.storage.local.UserStorage
import ru.bratusev.data.storage.local.UserStorageImpl
import ru.bratusev.data.storage.local.db.FilmDataBase
import ru.bratusev.data.storage.remote.RemoteFilmStorage
import ru.bratusev.data.storage.remote.RemoteFilmStorageImpl
import ru.bratusev.domain.repository.FilmRepository
import ru.bratusev.domain.repository.UserRepository

val dataModule =
    module {

        single<RemoteFilmStorage> {
            RemoteFilmStorageImpl()
        }

        single<FilmRepository> {
            FilmRepositoryImpl(remoteFilmStorage = get(), localFilmStorage = get())
        }

        single<UserRepository> {
            UserRepositoryImpl(userStorage = get())
        }

        single<UserStorage> {
            UserStorageImpl(context = get())
        }

        single<LocalFilmStorage> {
            FilmStorageImpl(filmDB = get())
        }

        single<FilmDataBase> {
            Room
                .databaseBuilder(context = get(), FilmDataBase::class.java, "films.db")
                .build()
        }
    }

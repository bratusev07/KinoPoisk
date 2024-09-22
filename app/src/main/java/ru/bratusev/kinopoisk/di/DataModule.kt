package ru.bratusev.kinopoisk.di

import org.koin.dsl.module
import ru.bratusev.data.repository.FilmRepositoryImpl
import ru.bratusev.data.storage.remote.RemoteFilmStorage
import ru.bratusev.data.storage.remote.RemoteFilmStorageImpl
import ru.bratusev.domain.repository.FilmRepository

val dataModule = module {

    single<RemoteFilmStorage> {
        RemoteFilmStorageImpl()
    }

    single<FilmRepository> {
        FilmRepositoryImpl(remoteFilmStorage = get())
    }
}
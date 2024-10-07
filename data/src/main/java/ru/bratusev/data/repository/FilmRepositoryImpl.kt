package ru.bratusev.data.repository

import ru.bratusev.data.model.FilmEntity
import ru.bratusev.data.model.toFilm
import ru.bratusev.data.model.toFilmDetail
import ru.bratusev.data.model.toFrame
import ru.bratusev.data.storage.local.LocalFilmStorage
import ru.bratusev.data.storage.remote.RemoteFilmStorage
import ru.bratusev.domain.model.Film
import ru.bratusev.domain.model.FilmDetail
import ru.bratusev.domain.model.FilmRequestParams
import ru.bratusev.domain.model.Frame
import ru.bratusev.domain.repository.FilmRepository

class FilmRepositoryImpl(
    private val remoteFilmStorage: RemoteFilmStorage,
    private val localFilmStorage: LocalFilmStorage
) : FilmRepository {

    override suspend fun getFilms(params: FilmRequestParams): List<Film> {
        return remoteFilmStorage.getFilmsRemote(ru.bratusev.data.model.FilmRequestParams(
            order = params.order,
            year = params.year,
            page = params.page,
            endYear = params.endYear
        )).map { it.toFilm() }
    }

    override suspend fun getFilmById(kinopoiskId: Int): FilmDetail {
        return remoteFilmStorage.getFilmByIdRemote(kinopoiskId).toFilmDetail()
    }

    override suspend fun getFilmFrames(kinopoiskId: Int): List<Frame> {
        return remoteFilmStorage.getFilmFramesRemote(kinopoiskId)
            .map { it.toFrame() }
    }

    override suspend fun insertFilmIntoDB(filmEntity: Film): Boolean {
        localFilmStorage.insertFilmIntoDB(
            FilmEntity(
                filmEntity.kinopoiskId,
                filmEntity.name.toString(),
                null,
                null,
                filmEntity.country,
                filmEntity.genre,
                filmEntity.ratingKinopoisk,
                filmEntity.year.toString(),
                null,
                null,
                filmEntity.posterUrlPreview,
            )
        )
        return true
    }

    override suspend fun getFilmsFromDB(): List<Film> {
        return localFilmStorage.getFilmsFromDB().map { it.toFilm() }
    }

    override suspend fun getFilmByKeyword(keyword: String): List<Film> {
        return localFilmStorage.getFilmByKeyword(keyword).map { it.toFilm() }
    }
}
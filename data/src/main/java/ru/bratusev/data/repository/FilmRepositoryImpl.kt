package ru.bratusev.data.repository

import ru.bratusev.data.model.toFilm
import ru.bratusev.data.model.toFilmDetail
import ru.bratusev.data.model.toFrame
import ru.bratusev.data.storage.remote.RemoteFilmStorage
import ru.bratusev.domain.model.Film
import ru.bratusev.domain.model.FilmDetail
import ru.bratusev.domain.model.Frame
import ru.bratusev.domain.repository.FilmRepository

class FilmRepositoryImpl(private val remoteFilmStorage: RemoteFilmStorage) : FilmRepository {

    override suspend fun getFilms(): ArrayList<Film> {
        return remoteFilmStorage.getFilmsRemote().map { it.toFilm() } as ArrayList<Film>
    }

    override suspend fun getFilmById(kinopoiskId: Int): FilmDetail {
        return remoteFilmStorage.getFilmByIdRemote(kinopoiskId).toFilmDetail()
    }

    /*    override suspend fun getFilmByKeyword(keyword: String): ArrayList<Film> {
            return remoteFilmStorage.getFilmsByKeywordRemote(keyword)
        }*/

    override suspend fun getFilmFrames(kinopoiskId: Int): ArrayList<Frame> {
        return remoteFilmStorage.getFilmFramesRemote(kinopoiskId)
            .map { it.toFrame() } as ArrayList<Frame>
    }
}
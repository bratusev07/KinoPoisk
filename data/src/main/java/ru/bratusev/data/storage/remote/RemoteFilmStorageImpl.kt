package ru.bratusev.data.storage.remote

import ru.bratusev.data.model.FilmDTO
import ru.bratusev.data.model.FilmDetailDTO
import ru.bratusev.data.model.FrameDTO
import ru.bratusev.data.storage.remote.common.Common

class RemoteFilmStorageImpl : RemoteFilmStorage {

    override suspend fun getFilmsRemote(): ArrayList<FilmDTO> {
        return Common.retrofitService.getFilms().items
    }

    override suspend fun getFilmsByKeywordRemote(keyword: String): ArrayList<FilmDTO> {
        return Common.retrofitService.getFilmsByKeyword(keyword).items
    }

    override suspend fun getFilmByIdRemote(kinopoiskId: Int): FilmDetailDTO {
        return Common.retrofitService.getFilmById(kinopoiskId)
    }

    override suspend fun getFilmFramesRemote(kinopoiskId: Int): ArrayList<FrameDTO> {
        return Common.retrofitService.getFilmFrames(kinopoiskId).items
    }
}
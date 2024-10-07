package ru.bratusev.data.storage.remote

import ru.bratusev.data.model.FilmDTO
import ru.bratusev.data.model.FilmDetailDTO
import ru.bratusev.data.model.FilmRequestParams
import ru.bratusev.data.model.FrameDTO
import ru.bratusev.data.storage.remote.common.Common

class RemoteFilmStorageImpl : RemoteFilmStorage {

    override suspend fun getFilmsRemote(params: FilmRequestParams): List<FilmDTO> {
        return Common.retrofitService.getFilms(params.order, params.year, params.page, params.endYear).items
    }

    override suspend fun getFilmsByKeywordRemote(keyword: String): List<FilmDTO> {
        return Common.retrofitService.getFilmsByKeyword(keyword).items
    }

    override suspend fun getFilmByIdRemote(kinopoiskId: Int): FilmDetailDTO {
        return Common.retrofitService.getFilmById(kinopoiskId)
    }

    override suspend fun getFilmFramesRemote(kinopoiskId: Int): List<FrameDTO> {
        return Common.retrofitService.getFilmFrames(kinopoiskId).items
    }
}
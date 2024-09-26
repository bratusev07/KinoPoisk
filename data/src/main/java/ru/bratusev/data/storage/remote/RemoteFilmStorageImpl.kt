package ru.bratusev.data.storage.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.bratusev.data.model.FilmDTO
import ru.bratusev.data.model.FilmDetailDTO
import ru.bratusev.data.model.FrameDTO
import ru.bratusev.data.storage.remote.common.Common

class RemoteFilmStorageImpl : RemoteFilmStorage {

    override suspend fun getFilmsRemote(order: String, year: String, page: Int, endYear: String): ArrayList<FilmDTO> {
        return withContext(Dispatchers.IO) {
            Common.retrofitService.getFilms(order, year, page, endYear).items
        }
    }

    override suspend fun getFilmsByKeywordRemote(keyword: String): ArrayList<FilmDTO> {
        return withContext(Dispatchers.IO) {
            Common.retrofitService.getFilmsByKeyword(keyword).items
        }
    }

    override suspend fun getFilmByIdRemote(kinopoiskId: Int): FilmDetailDTO {
        return withContext(Dispatchers.IO) {
            Common.retrofitService.getFilmById(kinopoiskId)
        }
    }

    override suspend fun getFilmFramesRemote(kinopoiskId: Int): ArrayList<FrameDTO> {
        return withContext(Dispatchers.IO) {
            Common.retrofitService.getFilmFrames(kinopoiskId).items
        }
    }
}
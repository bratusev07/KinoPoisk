package ru.bratusev.data.storage.remote

import ru.bratusev.data.model.FilmDTO
import ru.bratusev.data.model.FilmDetailDTO
import ru.bratusev.data.model.FilmRequestParams
import ru.bratusev.data.model.FrameDTO

interface RemoteFilmStorage {
    suspend fun getFilmsRemote(params: FilmRequestParams): List<FilmDTO>

    suspend fun getFilmsByKeywordRemote(keyword: String): List<FilmDTO>

    suspend fun getFilmByIdRemote(kinopoiskId: Int): FilmDetailDTO

    suspend fun getFilmFramesRemote(kinopoiskId: Int): List<FrameDTO>
}

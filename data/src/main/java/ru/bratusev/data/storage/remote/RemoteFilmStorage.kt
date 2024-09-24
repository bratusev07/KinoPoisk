package ru.bratusev.data.storage.remote

import ru.bratusev.data.model.FilmDTO
import ru.bratusev.data.model.FilmDetailDTO
import ru.bratusev.data.model.FrameDTO

interface RemoteFilmStorage {

    suspend fun getFilmsRemote(order: String, year: String, page: Int): ArrayList<FilmDTO>

    suspend fun getFilmsByKeywordRemote(keyword: String): ArrayList<FilmDTO>

    suspend fun getFilmByIdRemote(kinopoiskId: Int): FilmDetailDTO

    suspend fun getFilmFramesRemote(kinopoiskId: Int): ArrayList<FrameDTO>
}
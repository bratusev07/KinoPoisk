package ru.bratusev.data.storage.remote

import ru.bratusev.data.model.FilmDTO
import ru.bratusev.data.model.FilmDetailDTO
import ru.bratusev.data.model.FilmRequestParams
import ru.bratusev.data.model.FrameDTO
import ru.bratusev.data.storage.remote.common.RetrofitServices
import javax.inject.Inject

class RemoteFilmStorageImpl
    @Inject
    constructor(
        private val retrofitServices: RetrofitServices,
    ) : RemoteFilmStorage {
        override suspend fun getFilmsRemote(params: FilmRequestParams): List<FilmDTO> =
            retrofitServices.getFilms(params.order, params.year, params.page, params.endYear).items

        override suspend fun getFilmsByKeywordRemote(keyword: String): List<FilmDTO> = retrofitServices.getFilmsByKeyword(keyword).items

        override suspend fun getFilmByIdRemote(kinopoiskId: Int): FilmDetailDTO = retrofitServices.getFilmById(kinopoiskId)

        override suspend fun getFilmFramesRemote(kinopoiskId: Int): List<FrameDTO> = retrofitServices.getFilmFrames(kinopoiskId).items
    }

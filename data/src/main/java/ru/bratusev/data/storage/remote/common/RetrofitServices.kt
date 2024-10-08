package ru.bratusev.data.storage.remote.common

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.bratusev.data.model.FilmData
import ru.bratusev.data.model.FilmDetailDTO
import ru.bratusev.data.model.FrameData

interface RetrofitServices {
    @GET("films")
    suspend fun getFilms(
        @Query("order") order: String,
        @Query("yearFrom") yearFrom: String,
        @Query("page") page: Int,
        @Query("yearTo") yearTo: String,
    ): FilmData

    @GET("films/search-by-keyword")
    suspend fun getFilmsByKeyword(
        @Query("keyword") keyword: String,
    ): FilmData

    @GET("films/{filmId}")
    suspend fun getFilmById(
        @Path("filmId") filmId: Int,
    ): FilmDetailDTO

    @GET("films/{filmId}/images")
    suspend fun getFilmFrames(
        @Path("filmId") filmId: Int,
    ): FrameData
}

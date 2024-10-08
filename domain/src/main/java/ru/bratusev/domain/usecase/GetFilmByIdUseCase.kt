package ru.bratusev.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.bratusev.domain.Resource
import ru.bratusev.domain.model.FilmDetail
import ru.bratusev.domain.repository.FilmRepository
import java.io.IOException

class GetFilmByIdUseCase(
    private val filmRepository: FilmRepository,
) {
    operator fun invoke(kinopoiskId: Int): Flow<Resource<FilmDetail>> =
        flow {
            try {
                emit(Resource.Loading())
                val data = filmRepository.getFilmById(kinopoiskId)
                emit(Resource.Success(data))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            }
        }
}

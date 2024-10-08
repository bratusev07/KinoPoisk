package ru.bratusev.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.bratusev.domain.Resource
import ru.bratusev.domain.model.Film
import ru.bratusev.domain.model.FilmRequestParams
import ru.bratusev.domain.repository.FilmRepository
import java.io.IOException

class GetFilmsUseCase(
    private val filmRepository: FilmRepository,
) {
    operator fun invoke(params: FilmRequestParams): Flow<Resource<List<Film>>> =
        flow {
            try {
                emit(Resource.Loading())
                val data = filmRepository.getFilms(params)
                emit(Resource.Success(data))
                data.forEach { filmRepository.insertFilmIntoDB(it) }
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            }
        }
}

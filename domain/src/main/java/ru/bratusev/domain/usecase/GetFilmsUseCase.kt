package ru.bratusev.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.bratusev.domain.Resource
import ru.bratusev.domain.model.Film
import ru.bratusev.domain.repository.FilmRepository
import java.io.IOException

class GetFilmsUseCase(private val filmRepository: FilmRepository) {

    operator fun invoke(order: String, year: String, page: Int): Flow<Resource<ArrayList<Film>>> = flow {
        try {
            emit(Resource.Loading())
            val data = filmRepository.getFilms(order, year, page)
            emit(Resource.Success(data))
            data.forEach { filmRepository.insertFilmIntoDB(it) }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

}
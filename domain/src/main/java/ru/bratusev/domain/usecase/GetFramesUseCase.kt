package ru.bratusev.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.bratusev.domain.Resource
import ru.bratusev.domain.model.Frame
import ru.bratusev.domain.repository.FilmRepository
import java.io.IOException

class GetFramesUseCase(private val filmRepository: FilmRepository) {

    operator fun invoke(kinopoiskId: Int): Flow<Resource<List<Frame>>> = flow {
        try {
            emit(Resource.Loading())
            val data = filmRepository.getFilmFrames(kinopoiskId)
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

}
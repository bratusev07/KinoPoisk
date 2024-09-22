package ru.bratusev.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.domain.Resource

class LoginUseCase() {

    operator fun invoke(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Success(true))
        /*try {
            emit(Resource.Loading())
            val data = filmRepository.getFilms()
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }*/
    }

}
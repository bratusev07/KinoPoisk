package ru.bratusev.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.domain.Resource
import ru.bratusev.domain.repository.UserRepository
import java.io.IOException
import java.time.LocalDateTime

class GetLoginTimeUseCase(
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<Resource<LocalDateTime>> =
        flow {
            try {
                emit(Resource.Loading())
                val data = userRepository.getUserData()
                emit(Resource.Success(data?.loginTime ?: LocalDateTime.now()))
            } catch (e: IOException) {
                emit(Resource.Error("Something went wrong. Try it later"))
            }
        }
}

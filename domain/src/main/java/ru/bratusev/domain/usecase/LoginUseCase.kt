package ru.bratusev.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.domain.Resource
import ru.bratusev.domain.model.UserData
import ru.bratusev.domain.repository.UserRepository
import java.io.IOException

class LoginUseCase(
    private val userRepository: UserRepository,
) {
    operator fun invoke(userData: UserData): Flow<Resource<Boolean>> =
        flow {
            try {
                emit(Resource.Loading())
                val data = checkUserData(userData)
                emit(Resource.Success(data))
            } catch (e: IOException) {
                emit(Resource.Error("Something went wrong. Try it later"))
            }
        }

    private fun checkUserData(userData: UserData): Boolean {
        val registeredUser = userRepository.getUserData()
        if (registeredUser.login.isEmpty()) {
            userRepository.saveUserData(userData)
            return true
        }
        if (registeredUser.login == userData.login && registeredUser.password == userData.password) return true
        return false
    }
}

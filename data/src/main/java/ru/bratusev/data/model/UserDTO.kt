package ru.bratusev.data.model

import ru.bratusev.domain.model.UserData

data class UserDTO(
    internal val login: String?,
    internal val password: String?,
)

fun UserDTO.toUserData(): UserData = UserData(login = login ?: "", password = password ?: "")

package ru.bratusev.domain.model

import java.time.LocalDateTime

data class UserData(
    val login: String,
    val password: String,
    val loginTime: LocalDateTime,
)

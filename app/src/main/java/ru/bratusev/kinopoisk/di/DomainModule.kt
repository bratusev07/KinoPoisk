package ru.bratusev.kinopoisk.di

import org.koin.dsl.module
import ru.bratusev.domain.usecase.GetFilmByIdUseCase
import ru.bratusev.domain.usecase.GetFilmsUseCase
import ru.bratusev.domain.usecase.GetFramesUseCase
import ru.bratusev.domain.usecase.LoginUseCase

val domainModule = module {

    factory<GetFilmsUseCase> {
        GetFilmsUseCase(filmRepository = get())
    }

    factory<GetFilmByIdUseCase> {
        GetFilmByIdUseCase(filmRepository = get())
    }

    factory<LoginUseCase> {
        LoginUseCase(userRepository = get())
    }

    factory<GetFramesUseCase> {
        GetFramesUseCase(filmRepository = get())
    }
}
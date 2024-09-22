package ru.bratusev.kinopoisk.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bratusev.kinopoisk.presentation.details.DetailViewModel
import ru.bratusev.kinopoisk.presentation.login.LoginViewModel
import ru.bratusev.kinopoisk.presentation.search.SearchViewModel

val appModule = module {

    viewModel<LoginViewModel> {
        LoginViewModel(loginUseCase = get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(getFilmsUseCase = get())
    }

    viewModel<DetailViewModel> {
        DetailViewModel(
            getFilmByIdUseCase = get(),
            getFramesUseCase = get()
        )
    }
}
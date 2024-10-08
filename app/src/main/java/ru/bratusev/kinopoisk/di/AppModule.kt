package ru.bratusev.kinopoisk.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.bratusev.domain.usecase.GetFilmByIdUseCase
import ru.bratusev.domain.usecase.GetFilmByKeywordUseCase
import ru.bratusev.domain.usecase.GetFilmsUseCase
import ru.bratusev.domain.usecase.GetFramesUseCase
import ru.bratusev.domain.usecase.LoginUseCase
import ru.bratusev.kinopoisk.presentation.details.DetailViewModel
import ru.bratusev.kinopoisk.presentation.login.LoginViewModel
import ru.bratusev.kinopoisk.presentation.search.SearchViewModel

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {
    @Provides
    fun provideLoginViewModel(loginUseCase: LoginUseCase): LoginViewModel = LoginViewModel(loginUseCase)

    @Provides
    fun provideSearchViewModel(
        getFilmsUseCase: GetFilmsUseCase,
        getFilmByKeywordUseCase: GetFilmByKeywordUseCase,
    ): SearchViewModel = SearchViewModel(getFilmsUseCase, getFilmByKeywordUseCase)

    @Provides
    fun provideDetailViewModel(
        getFilmByIdUseCase: GetFilmByIdUseCase,
        getFramesUseCase: GetFramesUseCase,
    ): DetailViewModel = DetailViewModel(getFilmByIdUseCase, getFramesUseCase)
}

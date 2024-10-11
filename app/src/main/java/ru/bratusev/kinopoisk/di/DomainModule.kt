package ru.bratusev.kinopoisk.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.bratusev.domain.repository.FilmRepository
import ru.bratusev.domain.repository.UserRepository
import ru.bratusev.domain.usecase.GetFilmByIdUseCase
import ru.bratusev.domain.usecase.GetFilmByKeywordUseCase
import ru.bratusev.domain.usecase.GetFilmsUseCase
import ru.bratusev.domain.usecase.GetFramesUseCase
import ru.bratusev.domain.usecase.GetLoginTimeUseCase
import ru.bratusev.domain.usecase.LoginUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun provideGetFilmsUseCase(filmRepository: FilmRepository): GetFilmsUseCase = GetFilmsUseCase(filmRepository)

    @Provides
    @Singleton
    fun provideGetFilmByIdUseCase(filmRepository: FilmRepository): GetFilmByIdUseCase = GetFilmByIdUseCase(filmRepository)

    @Provides
    @Singleton
    fun provideLoginUseCase(userRepository: UserRepository): LoginUseCase = LoginUseCase(userRepository)

    @Provides
    @Singleton
    fun provideGetLoginTimeUseCase(userRepository: UserRepository): GetLoginTimeUseCase = GetLoginTimeUseCase(userRepository)

    @Provides
    @Singleton
    fun provideGetFramesUseCase(filmRepository: FilmRepository): GetFramesUseCase = GetFramesUseCase(filmRepository)

    @Provides
    @Singleton
    fun provideGetFilmByKeywordUseCase(filmRepository: FilmRepository): GetFilmByKeywordUseCase = GetFilmByKeywordUseCase(filmRepository)
}

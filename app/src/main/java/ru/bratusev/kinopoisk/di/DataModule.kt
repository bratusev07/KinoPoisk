package ru.bratusev.kinopoisk.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.bratusev.data.repository.FilmRepositoryImpl
import ru.bratusev.data.repository.UserRepositoryImpl
import ru.bratusev.data.storage.local.FilmStorageImpl
import ru.bratusev.data.storage.local.LocalFilmStorage
import ru.bratusev.data.storage.local.UserStorage
import ru.bratusev.data.storage.local.UserStorageImpl
import ru.bratusev.data.storage.local.db.FilmDataBase
import ru.bratusev.data.storage.remote.RemoteFilmStorage
import ru.bratusev.data.storage.remote.RemoteFilmStorageImpl
import ru.bratusev.domain.repository.FilmRepository
import ru.bratusev.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideRemoteFilmStorage(): RemoteFilmStorage = RemoteFilmStorageImpl()

    @Provides
    @Singleton
    fun provideFilmRepository(
        remoteFilmStorage: RemoteFilmStorage,
        localFilmStorage: LocalFilmStorage,
    ): FilmRepository = FilmRepositoryImpl(remoteFilmStorage, localFilmStorage)

    @Provides
    @Singleton
    fun provideUserRepository(userStorage: UserStorage): UserRepository = UserRepositoryImpl(userStorage)

    @Provides
    @Singleton
    fun provideUserStorage(
        @ApplicationContext context: Context,
    ): UserStorage = UserStorageImpl(context)

    @Provides
    @Singleton
    fun provideLocalFilmStorage(filmDB: FilmDataBase): LocalFilmStorage = FilmStorageImpl(filmDB)

    @Provides
    @Singleton
    fun provideFilmDataBase(
        @ApplicationContext context: Context,
    ): FilmDataBase =
        Room
            .databaseBuilder(context, FilmDataBase::class.java, "films.db")
            .build()
}

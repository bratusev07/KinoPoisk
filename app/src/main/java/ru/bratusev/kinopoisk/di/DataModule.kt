package ru.bratusev.kinopoisk.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.bratusev.data.converter.LocalDateTimeConverter
import ru.bratusev.data.repository.FilmRepositoryImpl
import ru.bratusev.data.repository.UserRepositoryImpl
import ru.bratusev.data.storage.local.FilmStorageImpl
import ru.bratusev.data.storage.local.LocalFilmStorage
import ru.bratusev.data.storage.local.UserStorage
import ru.bratusev.data.storage.local.UserStorageImpl
import ru.bratusev.data.storage.local.db.FilmDataBase
import ru.bratusev.data.storage.remote.RemoteFilmStorage
import ru.bratusev.data.storage.remote.RemoteFilmStorageImpl
import ru.bratusev.data.storage.remote.common.RetrofitServices
import ru.bratusev.domain.repository.FilmRepository
import ru.bratusev.domain.repository.UserRepository
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideRemoteFilmStorage(retrofitServices: RetrofitServices): RemoteFilmStorage =
        RemoteFilmStorageImpl(retrofitServices = retrofitServices)

    @Provides
    @Singleton
    fun provideFilmRepository(
        remoteFilmStorage: RemoteFilmStorage,
        localFilmStorage: LocalFilmStorage,
    ): FilmRepository = FilmRepositoryImpl(remoteFilmStorage, localFilmStorage)

    @Provides
    @Singleton
    fun provideUserRepository(
        userStorage: UserStorage,
        gson: Gson,
    ): UserRepository = UserRepositoryImpl(userStorage, gson)

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

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeConverter()).create()
}

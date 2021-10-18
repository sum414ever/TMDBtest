package com.better.tmdbtest.di

import android.content.Context
import androidx.room.Room
import com.better.tmdbtest.BuildConfig
import com.better.tmdbtest.data.FilmsRepositoryImpl
import com.better.tmdbtest.data.db.FilmDao
import com.better.tmdbtest.data.db.FilmDatabase
import com.better.tmdbtest.data.net.FilmApi
import com.better.tmdbtest.data.net.interceptor.TMDBApiAuthInterceptor
import com.better.tmdbtest.data.prefs.PreferencesAPI
import com.better.tmdbtest.domain.FilmsRepository
import com.better.tmdbtest.domain.usecase.MainActivityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dagger hilt object class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModules {
    /**
     * Creates an instance of [PreferencesAPI]
     */
    @Singleton
    @Provides
    fun providePreferencesAPI(
        @ApplicationContext context: Context
    ) = PreferencesAPI(context)

    /**
     * Creates an instance of [FilmsRepositoryImpl]
     */
    @Singleton
    @Provides
    fun provideRepository(
        prefs: PreferencesAPI,
        dao: FilmDao,
        api: FilmApi
    ) = FilmsRepositoryImpl(prefs, dao, api) as FilmsRepository

    // DI function that provides the MainActivityUseCase object.
    @Singleton
    @Provides
    fun provideMainActivityUseCase(
        filmsRepository: FilmsRepository
    ) = MainActivityUseCase(filmsRepository)

    // DI function that provides the retrofit client object.
    @Singleton
    @Provides
    fun provideFilmApi(): FilmApi {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(TMDBApiAuthInterceptor())

        if (BuildConfig.DEBUG)
            okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        val okHttpClient = okHttpClientBuilder.build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
            .create(FilmApi::class.java)
    }

    /**
     * Creates an instance of [FilmDatabase]
     */
    @Singleton
    @Provides
    fun provideFilmDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, FilmDatabase::class.java, "films_database")
            .build()

    /**
     * Creates an instance of [FilmDao]
     */
    @Singleton
    @Provides
    fun provideFilmDao(
        database: FilmDatabase
    ) = database.filmDao()
}
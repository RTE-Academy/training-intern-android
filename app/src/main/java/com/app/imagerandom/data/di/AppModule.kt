package com.app.imagerandom.data.di

import android.content.Context
import androidx.room.Room
import com.app.imagerandom.data.local.SharedPrefHelper
import com.app.imagerandom.data.local.dao.GenreDao
import com.app.imagerandom.data.local.db.AppDatabase
import com.app.imagerandom.data.network.MovieApiService
import com.app.imagerandom.data.repository.auth.AuthRepository
import com.app.imagerandom.data.repository.auth.AuthRepositoryImpl
import com.app.imagerandom.data.repository.categories.CategoriesRepository
import com.app.imagerandom.data.repository.categories.CategoriesRepositoryImpl
import com.app.imagerandom.data.repository.genre.GenreRepository
import com.app.imagerandom.data.repository.genre.GenreRepositoryImpl
import com.app.imagerandom.data.repository.movies.MoviesRepository
import com.app.imagerandom.data.repository.movies.MoviesRepositoryImpl
import com.app.imagerandom.data.repository.search.SearchRepository
import com.app.imagerandom.data.repository.search.SearchRepositoryImpl
import com.app.imagerandom.data.repository.tv_show.TvShowRepository
import com.app.imagerandom.data.repository.tv_show.TvShowRepositoryImpl
import com.app.imagerandom.data.repository.person.PersonRepository
import com.app.imagerandom.data.repository.person.PersonRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSharedPrefHelper(
        @ApplicationContext context: Context
    ): SharedPrefHelper {
        return SharedPrefHelper(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: MovieApiService): AuthRepository {
        return AuthRepositoryImpl(apiService = api)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(api: MovieApiService): MoviesRepository {
        return MoviesRepositoryImpl(apiService = api)
    }

    @Provides
    @Singleton
    fun provideCategoriesRepository(api: MovieApiService): CategoriesRepository {
        return CategoriesRepositoryImpl(apiService = api)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGenreDao(db: AppDatabase): GenreDao = db.genreDao()

    @Provides
    @Singleton
    fun provideGenreRepository(dao: GenreDao, api: MovieApiService): GenreRepository {
        return GenreRepositoryImpl(dao = dao, apiService = api)
    }

    @Provides
    @Singleton
    fun provideTvShowRepository(api: MovieApiService): TvShowRepository {
        return TvShowRepositoryImpl(apiService = api)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(api: MovieApiService): SearchRepository {
        return SearchRepositoryImpl(apiService = api)
    }

    @Provides
    @Singleton
    fun providePersonRepository(api: MovieApiService): PersonRepository {
        return PersonRepositoryImpl(apiService = api)
    }
}
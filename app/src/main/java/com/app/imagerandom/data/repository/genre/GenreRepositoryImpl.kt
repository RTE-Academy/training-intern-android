package com.app.imagerandom.data.repository.genre

import com.app.imagerandom.data.local.dao.GenreDao
import com.app.imagerandom.data.local.entity.GenreEntity
import com.app.imagerandom.data.network.MovieApiService
import com.app.imagerandom.domain.model.Genre
import com.app.imagerandom.domain.model.GenreListResponse
import javax.inject.Inject

class GenreRepositoryImpl @Inject constructor(
    private val dao: GenreDao,
    private val apiService: MovieApiService
) : GenreRepository {

    override fun getAllGenres(): List<Genre> {
        return dao.getAllGenres().map { it.toDomain() }
    }

    override suspend fun saveGenres(genres: List<Genre>) {
        dao.clearAll()
        dao.insertAll(genres.map { GenreEntity.fromDomain(it) })
    }

    override suspend fun getMovieGenreList(): GenreListResponse {
        return apiService.getMovieGenres()
    }
}
package com.app.imagerandom.data.repository.genre

import com.app.imagerandom.data.local.dao.GenreDao
import com.app.imagerandom.data.local.entity.GenreEntity
import com.app.imagerandom.domain.model.Genre
import javax.inject.Inject

class GenreRepositoryImpl @Inject constructor(
    private val dao: GenreDao,
) : GenreRepository {

    override fun getAllGenres(): List<Genre> {
        return dao.getAllGenres().map { it.toDomain() }
    }

    override suspend fun saveGenres(genres: List<Genre>) {
        dao.clearAll()
        dao.insertAll(genres.map { GenreEntity.fromDomain(it) })
    }
}
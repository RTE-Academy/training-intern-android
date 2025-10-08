package com.app.imagerandom.data.repository.genre

import com.app.imagerandom.domain.model.Genre

interface GenreRepository {
    fun getAllGenres(): List<Genre>
    suspend fun saveGenres(genres: List<Genre>)
}
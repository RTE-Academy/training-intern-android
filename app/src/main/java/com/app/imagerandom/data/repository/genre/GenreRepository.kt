package com.app.imagerandom.data.repository.genre

import com.app.imagerandom.domain.model.Genre
import com.app.imagerandom.domain.model.GenreListResponse

interface GenreRepository {
    fun getAllGenres(): List<Genre>
    suspend fun saveGenres(genres: List<Genre>)
    suspend fun getMovieGenreList(): GenreListResponse
}
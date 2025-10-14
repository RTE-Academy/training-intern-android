package com.app.imagerandom.domain.usecase.genres

import com.app.imagerandom.data.repository.genre.GenreRepository
import com.app.imagerandom.domain.model.GenreListResponse
import javax.inject.Inject

class FetchMovieGenreListUseCase @Inject constructor(
    private val repo: GenreRepository
) {
    // Fetch genres list
    suspend fun fetchMovieGenreList(): GenreListResponse = repo.getMovieGenreList()
}
package com.app.imagerandom.domain.usecase.genres

import com.app.imagerandom.data.repository.genre.GenreRepository
import com.app.imagerandom.domain.model.Genre
import javax.inject.Inject

class GetMovieGenreListUseCase @Inject constructor(
    private val repo: GenreRepository
) {
    fun getAllGenres(): List<Genre> = repo.getAllGenres()
}
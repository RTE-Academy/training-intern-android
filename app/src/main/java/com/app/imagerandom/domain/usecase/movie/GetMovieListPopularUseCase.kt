package com.app.imagerandom.domain.usecase.movie

import com.app.imagerandom.data.repository.movies.MoviesRepository
import com.app.imagerandom.domain.model.GetMovieListResponse
import javax.inject.Inject

class GetMovieListPopularUseCase @Inject constructor(
    private val repo: MoviesRepository
) {
    suspend fun getMovieListPopular(language: String, page: Int): GetMovieListResponse =
        repo.getMovieListPopular(language, page)
}
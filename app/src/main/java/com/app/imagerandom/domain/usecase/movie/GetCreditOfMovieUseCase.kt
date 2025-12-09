package com.app.imagerandom.domain.usecase.movie

import com.app.imagerandom.data.repository.movies.MoviesRepository
import com.app.imagerandom.domain.model.MovieCreditsResponse
import javax.inject.Inject

class GetCreditOfMovieUseCase @Inject constructor(
    private val repo: MoviesRepository
) {
    suspend fun getCreditOfMovie(
        movieId: Int
    ): MovieCreditsResponse = repo.getMovieCredits(movieId = movieId)
}
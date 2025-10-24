package com.app.imagerandom.domain.usecase.movie

import com.app.imagerandom.data.repository.movies.MoviesRepository
import javax.inject.Inject

class GetTrailerOfMovieUseCase @Inject constructor(
    private val repo: MoviesRepository
) {
    suspend fun getTrailerOfAMovie(movieId: Int) =
        repo.getMovieVideos(movieId = movieId)
}
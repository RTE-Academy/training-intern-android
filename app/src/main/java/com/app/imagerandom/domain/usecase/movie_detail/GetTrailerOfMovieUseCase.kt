package com.app.imagerandom.domain.usecase.movie_detail

import com.app.imagerandom.data.repository.movies.MoviesRepository
import com.app.imagerandom.domain.model.MovieVideosResponse
import javax.inject.Inject

class GetTrailerOfMovieUseCase @Inject constructor(
    private val repo: MoviesRepository
) {
    suspend fun getTrailerOfAMovie(
        movieId: Int
    ): MovieVideosResponse = repo.getMovieVideos(movieId = movieId)
}
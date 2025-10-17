package com.app.imagerandom.domain.usecase.movie_detail

import com.app.imagerandom.data.repository.movies.MoviesRepository
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val repo: MoviesRepository
) {
    suspend fun getMovieDetail(movieId: Int) =
        repo.getMovieDetail(movieId)
}
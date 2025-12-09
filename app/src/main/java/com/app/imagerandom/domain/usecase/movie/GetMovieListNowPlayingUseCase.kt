package com.app.imagerandom.domain.usecase.movie

import com.app.imagerandom.data.repository.movies.MoviesRepository
import com.app.imagerandom.domain.model.GetMovieListResponse
import javax.inject.Inject

class GetMovieListNowPlayingUseCase @Inject constructor(
    private val repo: MoviesRepository
) {
    suspend fun getMovieListNowPlaying(language: String, page: Int): GetMovieListResponse =
        repo.getMovieListNowPlaying(language, page)
}
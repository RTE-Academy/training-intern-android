package com.app.imagerandom.data.repository.movies

import com.app.imagerandom.data.network.MovieApiService
import com.app.imagerandom.domain.model.GetMovieListResponse
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.domain.model.MovieVideosResponse
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
) : MoviesRepository {
    override suspend fun getMovieList(language: String, page: Int): GetMovieListResponse {
        return apiService.getMovieList(language, page)
    }

    override suspend fun getMovieVideos(movieId: Int): MovieVideosResponse {
        return apiService.getMovieVideos(movieId)
    }

    override suspend fun getMovieCredits(movieId: Int): MovieCreditsResponse {
        return apiService.getMovieCredits(movieId)
    }
}
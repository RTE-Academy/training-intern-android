package com.app.imagerandom.domain.usecase.home

import com.app.imagerandom.data.repository.movies.MoviesRepository
import com.app.imagerandom.domain.model.GetMovieListResponse
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val repo: MoviesRepository
) {
    suspend fun getMovieList(language: String, page: Int): GetMovieListResponse {
        val sessionResult = repo.getMovieList(language, page)
        return sessionResult
    }
}
package com.app.imagerandom.data.repository.movies

import com.app.imagerandom.domain.model.GetMovieListResponse

interface MoviesRepository {
    suspend fun getMovieList(language: String, page: Int): GetMovieListResponse
}
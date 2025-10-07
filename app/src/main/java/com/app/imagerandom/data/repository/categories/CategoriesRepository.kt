package com.app.imagerandom.data.repository.categories

import com.app.imagerandom.domain.model.GetMovieListResponse

interface CategoriesRepository {
    suspend fun getMovieListByGenres(language: String, page: Int, genres: Int): GetMovieListResponse
}
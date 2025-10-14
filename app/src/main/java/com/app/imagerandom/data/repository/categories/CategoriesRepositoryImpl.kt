package com.app.imagerandom.data.repository.categories

import com.app.imagerandom.data.network.MovieApiService
import com.app.imagerandom.domain.model.GetMovieListResponse
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
) : CategoriesRepository {
    override suspend fun getMovieListByGenres(
        language: String,
        page: Int,
        genres: Int
    ): GetMovieListResponse {
        return apiService.getMovieListByGenres(language, page, genres)
    }
}
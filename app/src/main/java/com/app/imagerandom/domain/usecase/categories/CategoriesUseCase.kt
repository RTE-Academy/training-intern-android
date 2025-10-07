package com.app.imagerandom.domain.usecase.categories

import com.app.imagerandom.data.repository.categories.CategoriesRepository
import com.app.imagerandom.domain.model.GetMovieListResponse
import javax.inject.Inject

class CategoriesUseCase @Inject constructor(
    private val repo: CategoriesRepository
) {
    suspend fun getMovieListByGenres(language: String, page: Int, genres: Int): GetMovieListResponse = repo.getMovieListByGenres(language, page, genres)
}
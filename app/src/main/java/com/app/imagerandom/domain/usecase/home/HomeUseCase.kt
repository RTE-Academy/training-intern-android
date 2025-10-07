package com.app.imagerandom.domain.usecase.home

import com.app.imagerandom.data.repository.movies.MoviesRepository
import com.app.imagerandom.domain.model.GenreListResponse
import com.app.imagerandom.domain.model.GetMovieListResponse
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val repo: MoviesRepository
) {
    suspend fun getMovieList(language: String, page: Int): GetMovieListResponse = repo.getMovieList(language, page)
    suspend fun getMovieGenreList(): GenreListResponse = repo.getMovieGenreList()
}
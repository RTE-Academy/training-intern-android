package com.app.imagerandom.domain.usecase.search

import com.app.imagerandom.data.repository.search.SearchRepository
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(
    private val repo: SearchRepository
) {
    suspend fun searchMovie(query: String, page: Int) =
        repo.searchMovie(query, page)
}
package com.app.imagerandom.data.repository.search

import com.app.imagerandom.domain.model.MovieSearchResult
import com.app.imagerandom.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchMovie(query: String, page: Int): Flow<Resource<List<MovieSearchResult>>>
}
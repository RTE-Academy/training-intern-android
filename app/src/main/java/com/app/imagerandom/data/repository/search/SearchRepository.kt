package com.app.imagerandom.data.repository.search

import com.app.imagerandom.domain.model.SearchResult
import com.app.imagerandom.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchMovie(query: String, page: Int): Flow<Response<List<SearchResult>>>
}
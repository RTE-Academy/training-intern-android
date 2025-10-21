package com.app.imagerandom.data.repository.search

import com.app.imagerandom.data.mapper.toDomain
import com.app.imagerandom.data.network.MovieApiService
import com.app.imagerandom.domain.model.SearchResult
import com.app.imagerandom.domain.model.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
) : SearchRepository {
    override suspend fun searchMovie(query: String, page: Int): Flow<Response<List<SearchResult>>> = flow {
        emit(Response.Loading())
        try {
            val response = apiService.searchMovie(query, page)
            val results = response.results
                .map { it.toDomain(response.totalPage) }

            emit(Response.Success(results))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Unexpected error"))
        }
    }
}

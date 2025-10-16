package com.app.imagerandom.data.repository.search

import com.app.imagerandom.data.mapper.toDomain
import com.app.imagerandom.data.network.MovieApiService
import com.app.imagerandom.domain.model.MovieSearchResult
import com.app.imagerandom.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
) : SearchRepository {
    override suspend fun searchMovie(query: String, page: Int): Flow<Resource<List<MovieSearchResult>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.searchMovie(query, page)
            val results = response.results
                .filter { it.mediaType == "movie" || it.mediaType == "tv" }
                .map { it.toDomain() }

            emit(Resource.Success(results))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected error"))
        }
    }
}

package com.app.imagerandom.data.repository.person

import com.app.imagerandom.data.network.MovieApiService
import com.app.imagerandom.domain.model.PersonResponse
import com.app.imagerandom.domain.model.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
) : PersonRepository {

    override fun getPopularPerson(language: String, page: Int): Flow<Response<PersonResponse>> =
        flow {
            emit(Response.Loading())
            try {
                val response = apiService.getPopularPerson(language, page)
                emit(Response.Success(response))
            } catch (e: Exception) {
                emit(Response.Error(e.localizedMessage ?: "Unknown error"))
            }
        }

    override fun getPersonDetailById(personId: Int) = flow {
        emit(Response.Loading())
        try {
            val response = apiService.getPersonDetail(personId)
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Unknown error"))
        }
    }

    override fun searchPerson(query: String, page: Int): Flow<Response<PersonResponse>> =
        flow {
            emit(Response.Loading())
            try {
                val response = apiService.searchPerson(query, page)
                emit(Response.Success(response))
            } catch (e: Exception) {
                emit(Response.Error(e.localizedMessage ?: "Unexpected error"))
            }
        }
}
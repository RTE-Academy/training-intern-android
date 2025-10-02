package com.app.imagerandom.data.repository.auth

import com.app.imagerandom.data.network.MovieApiService
import com.app.imagerandom.domain.model.CreateSessionRequest
import com.app.imagerandom.domain.model.CreateSessionResponse
import com.app.imagerandom.domain.model.RequestTokenResponse
import com.app.imagerandom.domain.model.ValidateRequestTokenRequest
import com.app.imagerandom.domain.model.ValidateRequestTokenResponse
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
) : AuthRepository {
    override suspend fun getRequestToken(): RequestTokenResponse {
        return apiService.getRequestToken()
    }

    override suspend fun validateRequestToken(username: String, password: String, requestToken: String): ValidateRequestTokenResponse {
        val request = ValidateRequestTokenRequest(
            username = username,
            password = password,
            requestToken = requestToken
        )
        return apiService.validateRequestToken(request)
    }

    override suspend fun createSessionToken(requestToken: String): CreateSessionResponse {
        val request = CreateSessionRequest(requestToken = requestToken)
        return apiService.createSession(request)
    }
}
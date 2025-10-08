package com.app.imagerandom.data.repository.auth

import com.app.imagerandom.domain.model.CreateSessionResponse
import com.app.imagerandom.domain.model.RequestTokenResponse
import com.app.imagerandom.domain.model.ValidateRequestTokenResponse

interface AuthRepository {
    suspend fun getRequestToken(): RequestTokenResponse
    suspend fun validateRequestToken(username: String, password: String, requestToken: String): ValidateRequestTokenResponse
    suspend fun createSessionToken(requestToken: String): CreateSessionResponse
}
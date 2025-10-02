package com.app.imagerandom.domain.usecase.auth

import com.app.imagerandom.data.repository.auth.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend fun startAuthFlow(username: String, password: String): String {
        // Get request token
        val requestToken = repo.getRequestToken().requestToken

        // Validate request token với user & pass
        val validateResult = repo.validateRequestToken(username, password, requestToken)
        if (!validateResult.isSuccess) {
            throw Exception("Xác thực request token thất bại")
        }

        // Create session token
        val sessionResult = repo.createSessionToken(requestToken)
        return sessionResult.sessionId
    }
}


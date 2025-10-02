package com.app.imagerandom.data.network

import com.app.imagerandom.domain.model.CreateSessionRequest
import com.app.imagerandom.domain.model.CreateSessionResponse
import com.app.imagerandom.domain.model.GetMovieListResponse
import retrofit2.http.GET
import com.app.imagerandom.domain.model.RequestTokenResponse
import com.app.imagerandom.domain.model.ValidateRequestTokenRequest
import com.app.imagerandom.domain.model.ValidateRequestTokenResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface MovieApiService {

    // Tao request token
    @GET("authentication/token/new")
    suspend fun getRequestToken(): RequestTokenResponse

    // Xac thuc request token voi username va password
    @POST("authentication/token/validate_with_login")
    suspend fun validateRequestToken(
        @Body body: ValidateRequestTokenRequest
    ): ValidateRequestTokenResponse

    // Tao session moi
    @POST("authentication/session/new")
    suspend fun createSession(
        @Body body: CreateSessionRequest
    ): CreateSessionResponse

    // Lay danh sach phim
    @GET("movie/popular")
    suspend fun getMovieList(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): GetMovieListResponse
}

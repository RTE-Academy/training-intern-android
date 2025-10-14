package com.app.imagerandom.data.network

import com.app.imagerandom.domain.model.CreateSessionRequest
import com.app.imagerandom.domain.model.CreateSessionResponse
import com.app.imagerandom.domain.model.GenreListResponse
import com.app.imagerandom.domain.model.GetMovieListResponse
import com.app.imagerandom.domain.model.RequestTokenResponse
import com.app.imagerandom.domain.model.ValidateRequestTokenRequest
import com.app.imagerandom.domain.model.ValidateRequestTokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MovieApiService {

    // Generate request token
    @GET("authentication/token/new")
    suspend fun getRequestToken(): RequestTokenResponse

    // Validate request token with username and password
    @POST("authentication/token/validate_with_login")
    suspend fun validateRequestToken(
        @Body body: ValidateRequestTokenRequest
    ): ValidateRequestTokenResponse

    // Create new session
    @POST("authentication/session/new")
    suspend fun createSession(
        @Body body: CreateSessionRequest
    ): CreateSessionResponse

    // Get popular movie list
    @GET("movie/popular")
    suspend fun getMovieList(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): GetMovieListResponse

    // Get movie list by genre
    @GET("discover/movie")
    suspend fun getMovieListByGenres(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("with_genres") genreId: Int? = null
    ): GetMovieListResponse

    // Get genre list
    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") language: String = "vi-VN"
    ): GenreListResponse
}
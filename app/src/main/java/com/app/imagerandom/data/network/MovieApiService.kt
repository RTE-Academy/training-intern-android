package com.app.imagerandom.data.network

import com.app.imagerandom.domain.model.CreateSessionRequest
import com.app.imagerandom.domain.model.CreateSessionResponse
import com.app.imagerandom.domain.model.GenreListResponse
import com.app.imagerandom.domain.model.GetMovieListResponse
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.domain.model.MovieDetail
import com.app.imagerandom.domain.model.MovieVideosResponse
import com.app.imagerandom.domain.model.MovieSearchResponse
import com.app.imagerandom.domain.model.RequestTokenResponse
import com.app.imagerandom.domain.model.ValidateRequestTokenRequest
import com.app.imagerandom.domain.model.ValidateRequestTokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

    // Get popular movie list popular
    @GET("movie/popular")
    suspend fun getMovieListPopular(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): GetMovieListResponse

    // Get popular movie list now playing
    @GET("movie/now_playing")
    suspend fun getMovieListNowPlaying(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): GetMovieListResponse

    // Get popular movie list top rated
    @GET("movie/top_rated")
    suspend fun getMovieListTopRated(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): GetMovieListResponse

    // Get popular movie list upcoming
    @GET("movie/upcoming")
    suspend fun getMovieListUpcoming(
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

    // Get movie detail
    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "vi-VN",
        @Query("append_to_response") appendToResponse: String = "credits,videos"
    ): MovieDetail

    // Get genre list
    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") language: String = "vi-VN"
    ): GenreListResponse

    // Get credit list
    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int
    ): MovieCreditsResponse

    // Get trailer
    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): MovieVideosResponse

    // Search movie
    @GET("search/multi")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "vi-VN"
    ): MovieSearchResponse
}
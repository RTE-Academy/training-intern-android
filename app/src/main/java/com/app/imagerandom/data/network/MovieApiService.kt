package com.app.imagerandom.data.network

import com.app.imagerandom.domain.model.CreateSessionRequest
import com.app.imagerandom.domain.model.CreateSessionResponse
import com.app.imagerandom.domain.model.GenreListResponse
import com.app.imagerandom.domain.model.GetMovieListResponse
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.domain.model.MovieDetail
import com.app.imagerandom.domain.model.MovieVideosResponse
import com.app.imagerandom.domain.model.SearchResponse
import com.app.imagerandom.domain.model.PersonDetail
import com.app.imagerandom.domain.model.PersonResponse
import com.app.imagerandom.domain.model.RequestTokenResponse
import com.app.imagerandom.domain.model.TVShowDetail
import com.app.imagerandom.domain.model.TVShowResponse
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

    // Get TV shows airing today
    @GET("tv/airing_today")
    suspend fun getAiringTodayTVShows(
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): TVShowResponse

    // Get TV shows on the air
    @GET("tv/on_the_air")
    suspend fun getOnTheAirTVShows(
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): TVShowResponse

    // Get popular TV shows
    @GET("tv/popular")
    suspend fun getPopularTVShows(
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): TVShowResponse

    // Get top rated TV shows
    @GET("tv/top_rated")
    suspend fun getTopRatedTVShows(
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): TVShowResponse

    // Get TV shows detail
    @GET("tv/{series_id}")
    suspend fun getTVShowDetail(
        @Path("series_id") seriesId: Int,
        @Query("language") language: String = "vi-VN"
    ): TVShowDetail

    // Search movie
    @GET("search/multi")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "vi-VN"
    ): SearchResponse

    // Get person
    @GET("person/popular")
    suspend fun getPopularPerson(
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): PersonResponse

    // Get person detail
    @GET("person/{person_id}")
    suspend fun getPersonDetail(
        @Path("person_id") personId: Int,
        @Query("language") language: String = "vi-VN",
        @Query("append_to_response") appendToResponse: String = "movie_credits,tv_credits,images"
    ): PersonDetail

    // Search movie
    @GET("search/person")
    suspend fun searchPerson(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "vi-VN"
    ): PersonResponse
}
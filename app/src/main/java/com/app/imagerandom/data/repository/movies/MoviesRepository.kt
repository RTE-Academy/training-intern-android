package com.app.imagerandom.data.repository.movies

import com.app.imagerandom.domain.model.GetMovieListResponse
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.domain.model.MovieDetail
import com.app.imagerandom.domain.model.MovieVideosResponse
import com.app.imagerandom.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getMovieList(language: String, page: Int): GetMovieListResponse
    suspend fun getMovieVideos(movieId: Int): MovieVideosResponse
    suspend fun getMovieCredits(movieId: Int): MovieCreditsResponse
    suspend fun getMovieDetail(movieId: Int): Flow<Response<MovieDetail>>
}
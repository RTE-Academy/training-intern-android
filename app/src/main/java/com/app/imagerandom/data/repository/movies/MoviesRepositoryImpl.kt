package com.app.imagerandom.data.repository.movies

import com.app.imagerandom.data.network.MovieApiService
import com.app.imagerandom.domain.model.GetMovieListResponse
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.domain.model.MovieDetail
import com.app.imagerandom.domain.model.MovieVideosResponse
import com.app.imagerandom.domain.model.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
) : MoviesRepository {
    override suspend fun getMovieList(language: String, page: Int): GetMovieListResponse {
        return apiService.getMovieList(language, page)
    }

    override suspend fun getMovieVideos(movieId: Int): MovieVideosResponse {
        return apiService.getMovieVideos(movieId)
    }

    override suspend fun getMovieCredits(movieId: Int): MovieCreditsResponse {
        return apiService.getMovieCredits(movieId)
    }

    override suspend fun getMovieDetail(movieId: Int): Flow<Response<MovieDetail>> = flow {
        emit(Response.Loading())
        try {
            val result = apiService.getMovieDetail(movieId)
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "Lỗi khi tải chi tiết phim"))
        }
    }.flowOn(Dispatchers.IO)
}
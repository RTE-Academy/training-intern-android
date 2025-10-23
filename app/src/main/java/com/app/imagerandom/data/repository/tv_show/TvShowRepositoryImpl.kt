package com.app.imagerandom.data.repository.tv_show

import com.app.imagerandom.data.network.MovieApiService
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.model.TVShowDetail
import com.app.imagerandom.domain.model.TVShowResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TvShowRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
) : TvShowRepository {
    override suspend fun getAiringTodayTVShows(page: Int): Flow<Response<TVShowResponse>> = flow {
        emit(Response.Loading())
        try {
            val result = apiService.getAiringTodayTVShows(page = page)
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "Lỗi khi tải danh sách TV show đang phát sóng"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getOnTheAirTVShows(page: Int): Flow<Response<TVShowResponse>> = flow {
        emit(Response.Loading())
        try {
            val result = apiService.getOnTheAirTVShows(page = page)
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "Lỗi khi tải danh sách TV show on the air"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getPopularTVShows(page: Int): Flow<Response<TVShowResponse>> = flow {
        emit(Response.Loading())
        try {
            val result = apiService.getPopularTVShows(page = page)
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "Lỗi khi tải danh sách TV show phổ biến"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getTopRatedTVShows(page: Int): Flow<Response<TVShowResponse>> = flow {
        emit(Response.Loading())
        try {
            val result = apiService.getTopRatedTVShows(page = page)
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "Lỗi khi tải danh sách TV show được đánh giá cao"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getTVShowDetail(seriesId: Int): Flow<Response<TVShowDetail>> = flow {
        emit(Response.Loading())
        try {
            val result = apiService.getTVShowDetail(seriesId)
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "Lỗi khi tải chi tiết TV show"))
        }
    }.flowOn(Dispatchers.IO)
}
package com.app.imagerandom.data.repository.tv_show

import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.model.TVShowDetail
import com.app.imagerandom.domain.model.TVShowResponse
import kotlinx.coroutines.flow.Flow

interface TvShowRepository {
    suspend fun getAiringTodayTVShows(page: Int): Flow<Response<TVShowResponse>>
    suspend fun getOnTheAirTVShows(page: Int): Flow<Response<TVShowResponse>>
    suspend fun getPopularTVShows(page: Int): Flow<Response<TVShowResponse>>
    suspend fun getTopRatedTVShows(page: Int): Flow<Response<TVShowResponse>>
    suspend fun getTVShowDetail(seriesId: Int): Flow<Response<TVShowDetail>>
}
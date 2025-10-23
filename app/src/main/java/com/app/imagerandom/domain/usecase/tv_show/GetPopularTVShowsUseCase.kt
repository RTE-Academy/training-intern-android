package com.app.imagerandom.domain.usecase.tv_show

import com.app.imagerandom.data.repository.tv_show.TvShowRepository
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.model.TVShowResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularTVShowsUseCase @Inject constructor(
    private val repo: TvShowRepository
) {
    suspend fun getPopularTVShows(page: Int): Flow<Response<TVShowResponse>> {
        return repo.getPopularTVShows(page)
    }
}
package com.app.imagerandom.domain.usecase.tv_show

import com.app.imagerandom.data.repository.tv_show.TvShowRepository
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.model.TVShowDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTVShowDetailUseCase @Inject constructor(
    private val repo: TvShowRepository
) {
    suspend fun getTVShowDetail(seriesId: Int): Flow<Response<TVShowDetail>> {
        return repo.getTVShowDetail(seriesId)
    }
}
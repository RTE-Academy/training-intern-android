package com.app.imagerandom.data.repository.credit

import com.app.imagerandom.data.network.MovieApiService
import com.app.imagerandom.domain.model.MovieCreditsResponse
import javax.inject.Inject

class CreditRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
) : CreditRepository {

    override suspend fun getMovieCredits(movieId: Int): MovieCreditsResponse {
        return apiService.getMovieCredits(movieId)
    }
}
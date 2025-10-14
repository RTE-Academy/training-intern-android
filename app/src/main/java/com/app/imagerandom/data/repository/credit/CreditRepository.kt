package com.app.imagerandom.data.repository.credit

import com.app.imagerandom.domain.model.MovieCreditsResponse

interface CreditRepository {
    suspend fun getMovieCredits(movieId: Int): MovieCreditsResponse
}
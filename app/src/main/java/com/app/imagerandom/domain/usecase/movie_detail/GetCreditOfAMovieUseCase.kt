package com.app.imagerandom.domain.usecase.movie_detail

import com.app.imagerandom.data.repository.credit.CreditRepository
import com.app.imagerandom.domain.model.MovieCreditsResponse
import javax.inject.Inject

class GetCreditOfAMovieUseCase @Inject constructor(
    private val repo: CreditRepository
) {
    suspend fun getCreditOfAnMovie(
        movieId: Int
    ): MovieCreditsResponse = repo.getMovieCredits(movieId = movieId)
}
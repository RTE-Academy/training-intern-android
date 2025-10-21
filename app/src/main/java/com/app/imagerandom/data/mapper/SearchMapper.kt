package com.app.imagerandom.data.mapper

import com.app.imagerandom.common.NetworkConstants
import com.app.imagerandom.domain.model.MovieSearchItem
import com.app.imagerandom.domain.model.MovieSearchResult

fun MovieSearchItem.toDomain(totalPage: Int): MovieSearchResult {
    return MovieSearchResult(
        id = id,
        title = title ?: name.orEmpty(),
        overview = overview,
        posterUrl = posterPath?.let { NetworkConstants.IMAGE_BASE_URL + it },
        backdropUrl = backdropPath?.let { NetworkConstants.IMAGE_BASE_URL + it },
        mediaType = mediaType,
        rating = voteAverage,
        totalPage = totalPage
    )
}
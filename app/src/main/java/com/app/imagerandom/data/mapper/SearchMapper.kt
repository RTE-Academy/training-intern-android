package com.app.imagerandom.data.mapper

import com.app.imagerandom.common.NetworkConstants
import com.app.imagerandom.domain.model.SearchItem
import com.app.imagerandom.domain.model.SearchResult

fun SearchItem.toDomain(totalPage: Int): SearchResult {
    return SearchResult(
        id = id,
        title = title ?: name.orEmpty(),
        name = name ?: "",
        overview = overview,
        posterUrl = posterPath?.let { NetworkConstants.IMAGE_BASE_URL + it },
        backdropUrl = backdropPath?.let { NetworkConstants.IMAGE_BASE_URL + it },
        profilePath = profilePath?.let { NetworkConstants.IMAGE_BASE_URL + it },
        mediaType = mediaType,
        rating = voteAverage,
        totalPage = totalPage
    )
}
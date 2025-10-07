package com.app.imagerandom.domain.model

data class Genre(
    val id: Int,
    val name: String
)

data class GenreListResponse(
    val genres: List<Genre>
)
package com.app.imagerandom.domain.model

import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieSearchItem>
)

data class MovieSearchItem(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("media_type") val mediaType: String,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
)

data class MovieSearchResult(
    val id: Int,
    val title: String,
    val overview: String?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val mediaType: String,
    val rating: Double
)
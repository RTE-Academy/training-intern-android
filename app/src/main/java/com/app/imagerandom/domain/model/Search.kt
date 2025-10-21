package com.app.imagerandom.domain.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<SearchItem>,
    @SerializedName("total_pages") val totalPage: Int,
    @SerializedName("total_results") val totalResult: Int
)

data class SearchItem(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("media_type") val mediaType: String = "",
    @SerializedName("release_date") val releaseDate: String? = "",
    @SerializedName("first_air_date") val firstAirDate: String? = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
)

data class SearchResult(
    val id: Int,
    val title: String,
    val name: String,
    val overview: String?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val profilePath: String?,
    val mediaType: String,
    val rating: Double,
    val totalPage: Int
)
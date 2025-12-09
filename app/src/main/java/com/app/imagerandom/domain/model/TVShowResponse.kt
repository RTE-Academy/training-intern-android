package com.app.imagerandom.domain.model

import com.google.gson.annotations.SerializedName

data class TVShowResponse(
    @SerializedName("page") val page: Int = 1,
    @SerializedName("results") val results: List<TVShow> = emptyList(),
    @SerializedName("total_pages") val totalPage: Int
)

data class TVShow(
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("first_air_date") val firstAirDate: String? = null,
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("origin_country") val originCountry: List<String> = emptyList(),
    @SerializedName("original_language") val originalLanguage: String = "",
    @SerializedName("original_name") val originalName: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0
) {
    fun toMovieItem(): MovieItem {
        return MovieItem(
            adult = false,
            backdropPath = backdropPath,
            genreIds = genreIds,
            id = id,
            originalLanguage = originalLanguage,
            originalTitle = originalName,
            overview = overview.ifEmpty { "" },
            popularity = popularity,
            posterPath = posterPath,
            releaseDate = firstAirDate ?: "",
            title = name,
            video = false,
            voteAverage = voteAverage,
            voteCount = voteCount
        )
    }
}
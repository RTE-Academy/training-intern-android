package com.app.imagerandom.domain.model

import com.google.gson.annotations.SerializedName

data class PersonDetail(
    @SerializedName("adult") val adult: Boolean = false,
    @SerializedName("also_known_as") val alsoKnownAs: List<String> = emptyList(),
    @SerializedName("biography") val biography: String = "",
    @SerializedName("birthday") val birthday: String? = null,
    @SerializedName("deathday") val deathday: String? = null,
    @SerializedName("gender") val gender: Int = 0,
    @SerializedName("homepage") val homepage: String? = null,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("imdb_id") val imdbId: String? = null,
    @SerializedName("known_for_department") val knownForDepartment: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("place_of_birth") val placeOfBirth: String? = null,
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("profile_path") val profilePath: String? = null,
    @SerializedName("movie_credits") val movieCredits: MovieCredits = MovieCredits(),
    @SerializedName("tv_credits") val tvCredits: TvCredits = TvCredits(),
    @SerializedName("images") val images: Images = Images()
)

data class MovieCredits(
    @SerializedName("cast") val cast: List<MovieCast> = emptyList(),
    @SerializedName("crew") val crew: List<MovieCrew> = emptyList()
)

data class MovieCast(
    @SerializedName("adult") val adult: Boolean = false,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerializedName("id") val id: Int = 0,
    @SerializedName("original_language") val originalLanguage: String = "",
    @SerializedName("original_title") val originalTitle: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("title") val title: String = "",
    @SerializedName("video") val video: Boolean = false,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("character") val character: String = "",
    @SerializedName("credit_id") val creditId: String = "",
    @SerializedName("order") val order: Int = 0
)

data class MovieCrew(
    @SerializedName("adult") val adult: Boolean = false,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerializedName("id") val id: Int = 0,
    @SerializedName("original_language") val originalLanguage: String = "",
    @SerializedName("original_title") val originalTitle: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("title") val title: String = "",
    @SerializedName("video") val video: Boolean = false,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("credit_id") val creditId: String = "",
    @SerializedName("department") val department: String = "",
    @SerializedName("job") val job: String = ""
)

data class TvCredits(
    @SerializedName("cast") val cast: List<TvCast> = emptyList(),
    @SerializedName("crew") val crew: List<TvCrew> = emptyList()
)

data class TvCast(
    @SerializedName("adult") val adult: Boolean = false,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerializedName("id") val id: Int = 0,
    @SerializedName("origin_country") val originCountry: List<String> = emptyList(),
    @SerializedName("original_language") val originalLanguage: String = "",
    @SerializedName("original_name") val originalName: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("first_air_date") val firstAirDate: String? = null,
    @SerializedName("name") val name: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("character") val character: String = "",
    @SerializedName("credit_id") val creditId: String = "",
    @SerializedName("episode_count") val episodeCount: Int = 0,
    @SerializedName("first_credit_air_date") val firstCreditAirDate: String? = null
)

data class TvCrew(
    @SerializedName("adult") val adult: Boolean = false,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerializedName("id") val id: Int = 0,
    @SerializedName("origin_country") val originCountry: List<String> = emptyList(),
    @SerializedName("original_language") val originalLanguage: String = "",
    @SerializedName("original_name") val originalName: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("first_air_date") val firstAirDate: String? = null,
    @SerializedName("name") val name: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("credit_id") val creditId: String = "",
    @SerializedName("department") val department: String = "",
    @SerializedName("episode_count") val episodeCount: Int = 0,
    @SerializedName("first_credit_air_date") val firstCreditAirDate: String? = null,
    @SerializedName("job") val job: String = ""
)

data class Images(
    @SerializedName("profiles") val profiles: List<ProfileImage> = emptyList()
)

data class ProfileImage(
    @SerializedName("aspect_ratio") val aspectRatio: Double = 0.0,
    @SerializedName("height") val height: Int = 0,
    @SerializedName("iso_3166_1") val iso31661: String? = null,
    @SerializedName("iso_639_1") val iso6391: String? = null,
    @SerializedName("file_path") val filePath: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("width") val width: Int = 0
)
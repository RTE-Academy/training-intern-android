package com.app.imagerandom.domain.model

import com.google.gson.annotations.SerializedName

data class TVShowDetail(
    @SerializedName("adult") val adult: Boolean = false,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("created_by") val createdBy: List<TvCreator> = emptyList(),
    @SerializedName("episode_run_time") val episodeRunTime: List<Int> = emptyList(),
    @SerializedName("first_air_date") val firstAirDate: String? = null,
    @SerializedName("genres") val genres: List<Genre> = emptyList(),
    @SerializedName("homepage") val homepage: String? = null,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("in_production") val inProduction: Boolean = false,
    @SerializedName("languages") val languages: List<String> = emptyList(),
    @SerializedName("last_air_date") val lastAirDate: String? = null,
    @SerializedName("last_episode_to_air") val lastEpisode: TvEpisode? = null,
    @SerializedName("name") val name: String = "",
    @SerializedName("next_episode_to_air") val nextEpisode: TvEpisode? = null,
    @SerializedName("networks") val networks: List<TvNetwork> = emptyList(),
    @SerializedName("number_of_episodes") val numberOfEpisodes: Int = 0,
    @SerializedName("number_of_seasons") val numberOfSeasons: Int = 0,
    @SerializedName("origin_country") val originCountry: List<String> = emptyList(),
    @SerializedName("original_language") val originalLanguage: String = "",
    @SerializedName("original_name") val originalName: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("production_companies") val productionCompanies: List<TvProductionCompany> = emptyList(),
    @SerializedName("production_countries") val productionCountries: List<TvProductionCountry> = emptyList(),
    @SerializedName("seasons") val seasons: List<TvSeason> = emptyList(),
    @SerializedName("spoken_languages") val spokenLanguages: List<TvSpokenLanguage> = emptyList(),
    @SerializedName("status") val status: String = "",
    @SerializedName("tagline") val tagline: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0
)

data class TvCreator(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("credit_id") val creditId: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("gender") val gender: Int = 0,
    @SerializedName("profile_path") val profilePath: String? = null
)

data class TvEpisode(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("air_date") val airDate: String? = null,
    @SerializedName("episode_number") val episodeNumber: Int = 0,
    @SerializedName("production_code") val productionCode: String = "",
    @SerializedName("runtime") val runtime: Int? = null,
    @SerializedName("season_number") val seasonNumber: Int = 0,
    @SerializedName("show_id") val showId: Int = 0,
    @SerializedName("still_path") val stillPath: String? = null
)

data class TvNetwork(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("logo_path") val logoPath: String? = null,
    @SerializedName("name") val name: String = "",
    @SerializedName("origin_country") val originCountry: String = ""
)

data class TvProductionCompany(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("logo_path") val logoPath: String? = null,
    @SerializedName("name") val name: String = "",
    @SerializedName("origin_country") val originCountry: String = ""
)

data class TvProductionCountry(
    @SerializedName("iso_3166_1") val iso31661: String = "",
    @SerializedName("name") val name: String = ""
)

data class TvSeason(
    @SerializedName("air_date") val airDate: String? = null,
    @SerializedName("episode_count") val episodeCount: Int = 0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("season_number") val seasonNumber: Int = 0,
    @SerializedName("vote_average") val voteAverage: Double = 0.0
)

data class TvSpokenLanguage(
    @SerializedName("english_name") val englishName: String = "",
    @SerializedName("iso_639_1") val iso6391: String = "",
    @SerializedName("name") val name: String = ""
)
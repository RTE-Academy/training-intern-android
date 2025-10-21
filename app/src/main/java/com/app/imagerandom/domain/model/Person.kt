package com.app.imagerandom.domain.model

import com.google.gson.annotations.SerializedName

data class PersonResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<Person>,
    @SerializedName("total_pages") val totalPage: Int
)

data class Person(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("known_for_department") val knownForDepartment: String = "",
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("known_for") val knownFor: List<KnownFor> = emptyList()
)

data class KnownFor(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("media_type") val mediaType: String
)
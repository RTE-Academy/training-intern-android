package com.app.imagerandom.domain.model

import com.google.gson.annotations.SerializedName

data class MovieCreditsResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("cast")
    val cast: List<Cast>,
    @SerializedName("crew")
    val crew: List<Crew>
)

data class Cast(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("character")
    val character: String?,
    @SerializedName("order")
    val order: Int?,
    @SerializedName("profile_path")
    val profilePath: String?
)

data class Crew(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("department")
    val department: String?,
    @SerializedName("job")
    val job: String?
)

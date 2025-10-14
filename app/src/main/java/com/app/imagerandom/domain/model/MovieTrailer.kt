package com.app.imagerandom.domain.model

import com.google.gson.annotations.SerializedName

data class MovieVideosResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<MovieVideo>
)

data class MovieVideo(
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("site") val site: String,
    @SerializedName("type") val type: String
)

package com.developer.amukovozov.nerd.model.movie

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Genres(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)
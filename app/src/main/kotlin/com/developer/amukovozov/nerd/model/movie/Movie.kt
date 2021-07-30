package com.developer.amukovozov.nerd.model.movie

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Movie(
    val id: Int,
    @Json(name = "poster_path")
    val posterPath: String?,
    val title: String
)
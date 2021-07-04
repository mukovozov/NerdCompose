package com.developer.amukovozov.nerd.model.movie

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductionCountries(
    @Json(name = "iso_3166_1") val countryCode: String,
    @Json(name = "name") val name: String
)
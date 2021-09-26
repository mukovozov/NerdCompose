package com.developer.amukovozov.nerd.model.movie

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AvailabilityInfo(
    @Json(name = "link") val link: String?,
    @Json(name = "buy") val whereToBuy: List<AvailablePlatformInfo>?,
    @Json(name = "rent") val whereToRent: List<AvailablePlatformInfo>?
)
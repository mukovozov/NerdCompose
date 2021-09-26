package com.developer.amukovozov.nerd.model.movie

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AvailablePlatformInfo(
    @Json(name = "display_priority") val displayPriority: Int?,
    @Json(name = "logo_path") val logoPath: String?,
    @Json(name = "provider_id") val providerId: Int?,
    @Json(name = "provider_name") val providerName: String?
)
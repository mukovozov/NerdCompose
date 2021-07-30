package com.developer.amukovozov.nerd.model.movie

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Crew(
    @Json(name = "adult") val adult: Boolean,
    @Json(name = "gender") val gender: Int,
    @Json(name = "id") val id: Int,
    @Json(name = "known_for_department") val knownForDepartment: String,
    @Json(name = "name") override val name: String,
    @Json(name = "original_name") override val originalName: String,
    @Json(name = "popularity") val popularity: Double,
    @Json(name = "profile_path") override val profilePath: String,
    @Json(name = "credit_id") val creditId: String,
    @Json(name = "department") val department: String,
    @Json(name = "job") val job: String,
    override val role: String = job
) : Member
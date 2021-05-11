package com.developer.amukovozov.nerd.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInfoDetails(
    @Json(name = "id")
    val id: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "links")
    val links: List<SocialMediaLink>?,
    @Json(name = "avatar_path")
    val avatarPath: String?
)
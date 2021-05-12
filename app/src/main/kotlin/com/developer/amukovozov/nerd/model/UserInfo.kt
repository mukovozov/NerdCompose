package com.developer.amukovozov.nerd.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInfo(
    val id: Int,
    val email: String,
    val nickname: String,

    @Json(name = "avatar_path")
    val avatarPath: String?
)
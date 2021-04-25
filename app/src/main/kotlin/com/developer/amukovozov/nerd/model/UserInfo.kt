package com.developer.amukovozov.nerd.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInfo(
    val id: Int,
    val email: String,
    val nickname: String
)
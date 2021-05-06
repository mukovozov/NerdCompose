package com.developer.amukovozov.nerd.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenInfo(
    val code: Int,
    val token: String
)
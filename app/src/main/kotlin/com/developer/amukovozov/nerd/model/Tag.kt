package com.developer.amukovozov.nerd.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tag(
    val id: Int,
    val title: String
)
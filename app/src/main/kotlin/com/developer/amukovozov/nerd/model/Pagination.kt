package com.developer.amukovozov.nerd.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Pagination<T>(
    val page: Int,
    val results: List<T>
)
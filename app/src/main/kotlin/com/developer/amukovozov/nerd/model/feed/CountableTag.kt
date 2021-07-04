package com.developer.amukovozov.nerd.model.feed

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountableTag(
    @Json(name = "tag")
    val tag: Tag,
    @Json(name = "count")
    val count: Int
)
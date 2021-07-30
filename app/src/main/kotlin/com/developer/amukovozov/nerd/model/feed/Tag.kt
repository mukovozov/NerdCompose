package com.developer.amukovozov.nerd.model.feed

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tag(
    val id: Int,
    val title: String,
    @Json(name = "emoji_code")
    val emojiCode: String?,
    @Json(name = "background_color")
    val backgroundColor: String?,
    @Json(name = "text_color")
    val textColor: String?
)
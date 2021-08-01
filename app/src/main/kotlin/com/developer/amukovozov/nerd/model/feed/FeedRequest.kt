package com.developer.amukovozov.nerd.model.feed

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedRequest(
    @Json(name = "movie_id")
    val movieId: Int,
    @Json(name = "user_review")
    val userReview: String,
    @Json(name = "type")
    val type: FeedType,
    @Json(name = "tags")
    val tags: List<Tag>?
)
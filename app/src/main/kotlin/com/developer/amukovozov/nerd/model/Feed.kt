package com.developer.amukovozov.nerd.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Feed(
    val id: Int,
    @Json(name = "is_liked")
    val isLiked: Boolean,
    @Json(name = "likes_count")
    val likesCount: Int,
    val type: FeedType,
    @Json(name = "creation_time")
    val creationTime: Long,
    @Json(name = "user_review")
    val userReview: String,
    val movie: Movie,
    val tags: List<Tag>?,
    @Json(name = "user")
    val userInfo: UserInfo
)

enum class FeedType {
    Review;

    companion object {
        val Default = Review
    }
}
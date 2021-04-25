package com.developer.amukovozov.nerd.model

data class Feed(
    val id: Int,
    val isLiked: Boolean,
    val likesCount: Int,
    val type: FeedType,
    val creationTime: Long,
    val userReview: String,
    val movie: Movie,
    val tags: List<Tag>?,
    val userInfo: UserInfo
)

enum class FeedType {
    Review
}
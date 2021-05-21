package com.developer.amukovozov.nerd.model

data class FullUserInfo(
    val userInfo: UserInfoDetails,
    val followings: Int,
    val followers: Int,
    val watchList: List<Movie>,
    val posts: List<Feed>
)
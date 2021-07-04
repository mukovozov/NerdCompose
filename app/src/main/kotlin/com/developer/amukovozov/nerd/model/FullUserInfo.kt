package com.developer.amukovozov.nerd.model

import com.developer.amukovozov.nerd.model.feed.Feed
import com.developer.amukovozov.nerd.model.movie.Movie

data class FullUserInfo(
    val userInfo: UserInfoDetails,
    val followings: Int,
    val followers: Int,
    val watchList: List<Movie>,
    val posts: List<Feed>
)
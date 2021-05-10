package com.developer.amukovozov.nerd.model

data class FullUserInfo(
    val userInfo: ProfileDetailsResponse,
    val followings: Int,
    val followers: Int
)
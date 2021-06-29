package com.developer.amukovozov.nerd.repository

import com.developer.amukovozov.nerd.network.NerdApi
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class FollowersRepository @Inject constructor(
    private val api: NerdApi
) {
    fun follow(userId: Int): Completable {
        return api.follow(userId)
    }

    fun unfollow(userId: Int): Completable {
        return api.unfollow(userId)
    }
}
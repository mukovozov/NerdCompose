package com.developer.amukovozov.nerd.repository

import com.developer.amukovozov.nerd.network.NerdApi
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

@ViewModelScoped
class LikeRepository @Inject constructor(
    private val api: NerdApi
) {
    fun addLike(feedId: Int): Completable {
        return api.addLike(feedId)
    }

    fun removeLike(feedId: Int): Completable {
        return api.removeLike(feedId)
    }
}
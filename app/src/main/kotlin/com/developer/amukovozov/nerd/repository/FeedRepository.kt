package com.developer.amukovozov.nerd.repository

import com.developer.amukovozov.nerd.model.feed.Feed
import com.developer.amukovozov.nerd.network.NerdApi
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class FeedRepository @Inject constructor(
    private val nerdApi: NerdApi
) {

    fun loadFeedPage(page: Int): Single<List<Feed>> {
        return nerdApi.getFeed(page)
            .map { it.results ?: emptyList() }
    }

    fun loadMyFeedPage(page: Int): Single<List<Feed>> {
        return nerdApi.getMyFeed(page)
            .map { it.results ?: emptyList() }
    }

    fun loadUserFeedPage(userId: Int, page: Int): Single<List<Feed>> {
        return nerdApi.getUserFeed(userId, page)
            .map { it.results ?: emptyList() }
    }
}
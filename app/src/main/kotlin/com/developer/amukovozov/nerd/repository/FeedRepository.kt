package com.developer.amukovozov.nerd.repository

import com.developer.amukovozov.nerd.model.Feed
import com.developer.amukovozov.nerd.network.NerdApi
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class FeedRepository @Inject constructor(
    private val nerdApi: NerdApi
) {

    fun loadFeed(): Single<List<Feed>> {
        return nerdApi.getFeed(0)
            .map { it.results }
    }
}
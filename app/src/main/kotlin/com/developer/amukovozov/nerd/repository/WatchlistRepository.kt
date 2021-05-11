package com.developer.amukovozov.nerd.repository

import com.developer.amukovozov.nerd.model.Movie
import com.developer.amukovozov.nerd.network.NerdApi
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class WatchlistRepository @Inject constructor(
    private val api: NerdApi
) {
    fun getMyWatchlist(): Single<List<Movie>> {
        return api.getMyWatchlist()
    }
}
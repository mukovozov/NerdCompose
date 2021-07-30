package com.developer.amukovozov.nerd.repository

import com.developer.amukovozov.nerd.model.movie.Movie
import com.developer.amukovozov.nerd.network.NerdApi
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class WatchlistRepository @Inject constructor(
    private val api: NerdApi
) {
    fun loadMyWatchlistByPage(page: Int): Single<List<Movie>> {
        return api.getMyWatchlist(page)
            .map { it.results ?: emptyList() }
    }

    fun loadUserWatchlistByPage(page: Int, userId: Int): Single<List<Movie>> {
        return api.getUserWatchlist(page, userId)
            .map { it.results ?: emptyList() }
    }

    fun addInWatchlist(movieId: Int): Completable {
        return api.addToWatchlist(movieId)
    }

    fun removeFromWatchlist(movieId: Int): Completable {
        return api.deleteFromWatchlist(movieId)
    }

}
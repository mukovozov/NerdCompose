package com.developer.amukovozov.nerd.repository

import com.developer.amukovozov.nerd.model.movie.MovieDetails
import com.developer.amukovozov.nerd.network.NerdApi
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class MovieDetailsRepository @Inject constructor(
    private val api: NerdApi
) {
    fun loadMovieDetails(movieId: Int): Single<MovieDetails> {
        return api.getMovieDetails(movieId)
    }
}
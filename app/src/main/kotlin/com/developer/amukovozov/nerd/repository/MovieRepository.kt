package com.developer.amukovozov.nerd.repository

import com.developer.amukovozov.nerd.model.movie.Movie
import com.developer.amukovozov.nerd.network.NerdApi
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class MovieRepository @Inject constructor(
    private val api: NerdApi
) {
    fun searchMovie(query: String): Single<List<Movie>> {
        return api.searchMovies(query)
    }

    fun getMovieShortInfo(id: Int): Single<Movie> {
        return api.getShortMovieInfo(id)
    }
}
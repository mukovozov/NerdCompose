package com.developer.amukovozov.nerd.ui.screens.movie_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.model.movie.MovieDetails
import com.developer.amukovozov.nerd.repository.MovieDetailsRepository
import com.developer.amukovozov.nerd.repository.WatchlistRepository
import com.developer.amukovozov.nerd.util.rx.schedulersIoToMain
import com.developer.amukovozov.nerd.util.ui.*
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository,
    private val watchlistRepository: WatchlistRepository
) : BaseViewModel() {

    var viewState by mutableStateOf(MovieDetailsViewState())

    fun onScreenOpened(movieId: Int) {
        loadMovieDetails(movieId)
    }

    fun onShareButtonClicked() {
        Timber.d("OnShareButtonClicked")
    }

    fun onWatchlistButtonClicked(movieId: Int, isInWatchlist: Boolean) {
        val updatedViewState = (viewState.screenState as Content).content?.copy(watchlist = isInWatchlist)
        viewState = viewState.copy(screenState = Content(updatedViewState))

        val source = if (isInWatchlist) {
            watchlistRepository.addInWatchlist(movieId)
        } else {
            watchlistRepository.removeFromWatchlist(movieId)
        }

        source
            .schedulersIoToMain()
            .subscribe({
            }, {
                Timber.e(it)
                val fallbackViewState = (viewState.screenState as Content).content?.copy(watchlist = !isInWatchlist)
                viewState = viewState.copy(screenState = Content(fallbackViewState))
            })
            .disposeOnViewModelDestroy()
    }

    private fun loadMovieDetails(movieId: Int) {
        movieDetailsRepository.loadMovieDetails(movieId)
            .toObservable()
            .map<ScreenState<MovieDetails>>(::Content)
            .doOnError(Timber::d)
            .onErrorReturn(::Stub)
            .startWithItem(Loading())
            .schedulersIoToMain()
            .subscribe { viewState = viewState.copy(screenState = it) }
            .disposeOnViewModelDestroy()
    }
}
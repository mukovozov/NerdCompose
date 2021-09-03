package com.developer.amukovozov.nerd.ui.screens.browse

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.model.BrowseInformation
import com.developer.amukovozov.nerd.model.feed.Tag
import com.developer.amukovozov.nerd.repository.MovieRepository
import com.developer.amukovozov.nerd.use_case.BrowseUseCase
import com.developer.amukovozov.nerd.util.rx.schedulersIoToMain
import com.developer.amukovozov.nerd.util.ui.*
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val browseUseCase: BrowseUseCase,
    private val movieRepository: MovieRepository
) : BaseViewModel() {
    var viewState by mutableStateOf(BrowseViewState())

    init {
        viewState = viewState.copy(isLoading = true)
        loadInfo()
    }

    fun onSearchQueryChanged(query: String) {
        if (query == "") {
            clearSearchResult(query)
        } else {
            searchMovie(query)
        }
    }

    fun onTagSelected(tag: Tag) {
        viewState = viewState.copy(selectedTag = tag, isLoading = true)
        movieRepository.searchMovieByTag(tag.id)
            .schedulersIoToMain()
            .subscribe({ movies ->
                viewState = viewState.copy(searchResult = movies, isLoading = false)
            }, {
                viewState = viewState.copy(isLoading = false)
                Timber.e(it)
            })
            .disposeOnViewModelDestroy()
    }

    fun onUserClicked(userId: Int) {

    }

    private fun clearSearchResult(query: String) {
        viewState = viewState.copy(
            searchField = query,
            isLoading = false,
            searchResult = emptyList()
        )
    }

    private fun searchMovie(query: String) {
        viewState = viewState.copy(searchField = query, isLoading = true)
        movieRepository.searchMovie(query)
            .schedulersIoToMain()
            .subscribe({
                viewState = viewState.copy(
                    searchResult = it,
                    isLoading = false
                )
            }, {
                viewState = viewState.copy(isLoading = false)
                Timber.e(it)
            })
            .disposeOnViewModelDestroy()
    }

    private fun loadInfo() {
        browseUseCase.loadBrowseScreenInfo()
            .toObservable()
            .map<ScreenState<BrowseInformation>> { Content(it) }
            .doOnError(Timber::e)
            .onErrorReturn(::Stub)
            .startWithItem(Loading())
            .schedulersIoToMain()
            .subscribe { screenState ->
                viewState = viewState.copy(browseInfoState = screenState)
            }
            .disposeOnViewModelDestroy()
    }

}
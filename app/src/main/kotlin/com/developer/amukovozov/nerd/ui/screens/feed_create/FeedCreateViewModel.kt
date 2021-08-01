package com.developer.amukovozov.nerd.ui.screens.feed_create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.model.feed.FeedRequest
import com.developer.amukovozov.nerd.model.feed.FeedType
import com.developer.amukovozov.nerd.model.feed.Tag
import com.developer.amukovozov.nerd.model.movie.Movie
import com.developer.amukovozov.nerd.repository.FeedRepository
import com.developer.amukovozov.nerd.repository.MovieRepository
import com.developer.amukovozov.nerd.repository.TagRepository
import com.developer.amukovozov.nerd.util.rx.schedulersIoToMain
import com.developer.amukovozov.nerd.util.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class FeedCreateViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val moviesRepository: MovieRepository,
    private val tagsRepository: TagRepository
) : BaseViewModel() {

    var isInited = false

    var viewState by mutableStateOf(FeedCreateViewState())

    init {
        loadTags()
    }

    fun onScreenOpened(movieId: Int?) {
        if (!isInited) {
            viewState = viewState.copy(movieId = movieId)
            if (movieId != null) {
                loadMovieInfo(movieId)
            }
            isInited = true
        }
    }

    fun onQueryChanged(query: String) {
        if (query == "") {
            val updatedMovieSearch = viewState.autoCompleteState.copy(
                filteredItems = emptyList(),
                query = query
            )
            viewState = viewState.copy(autoCompleteState = updatedMovieSearch, isNextButtonEnabled = false)
        } else {
            val updated = viewState.autoCompleteState.copy(
                query = query,
            )
            viewState = viewState.copy(autoCompleteState = updated, isNextButtonEnabled = true)

            moviesRepository.searchMovie(query)
                .schedulersIoToMain()
                .subscribe({ list ->
                    val updatedMovieSearch = viewState.autoCompleteState.copy(
                        filteredItems = list
                    )
                    viewState = viewState.copy(autoCompleteState = updatedMovieSearch)
                }, Timber::d)
                .disposeOnViewModelDestroy()
        }
    }

    fun onMovieSelected(movie: Movie) {
        viewState = viewState.copy(
            movieId = movie.id,
            movie = movie,
            isNextButtonEnabled = true
        )
        onStepFinished()
    }

    fun onTagSelected(tag: Tag, isSelected: Boolean) {
        val updatedList = viewState.tags.map {
            if (it.tag == tag) {
                TagState(tag, isSelected)
            } else {
                it
            }
        }
        val isNextButtonAvailable = viewState.tags.any { it.isSelected }
        viewState = viewState.copy(tags = updatedList, isNextButtonEnabled = isNextButtonAvailable)
    }

    fun onTagAdded(tag: Tag) {
        val updatedList = viewState.tags.plus(TagState(tag, true))
        viewState = viewState.copy(tags = updatedList, isNextButtonEnabled = true)
    }

    fun onStepFinished() {
        val nextStep = viewState.progressIndex + 1
        if (nextStep == 3) {
            onPublishReviewButtonClicked()
        } else {
            viewState = viewState.copy(progressIndex = nextStep)
        }
    }

    private fun onPublishReviewButtonClicked() {
        val movieId = viewState.movieId ?: return
        val review = viewState.review.text
        val tags = viewState.tags.filter { it.isSelected }.map { it.tag }
        val feed = FeedRequest(movieId, review, FeedType.Review, tags)

        feedRepository.createFeed(feed)
            .schedulersIoToMain()
            .subscribe({
                viewState = viewState.copy(isReviewPublished = true)
            }, {
                Timber.e(it)
            })
            .disposeOnViewModelDestroy()
    }

    private fun loadMovieInfo(movieId: Int) {
        moviesRepository.getMovieShortInfo(movieId)
            .schedulersIoToMain()
            .subscribe({
                val updatedState = viewState.autoCompleteState.copy(query = it.title)
                viewState = viewState.copy(
                    movie = it,
                    autoCompleteState = updatedState,
                    isNextButtonEnabled = true
                )
            }, Timber::d)
            .disposeOnViewModelDestroy()
    }

    private fun loadTags() {
        tagsRepository.loadTags()
            .schedulersIoToMain()
            .subscribe({
                val tagStates = it.map { tag -> TagState(tag, false) }
                viewState = viewState.copy(tags = tagStates)
            }, Timber::d)
            .disposeOnViewModelDestroy()
    }
}
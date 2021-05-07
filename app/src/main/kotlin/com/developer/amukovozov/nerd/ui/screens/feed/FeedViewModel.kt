package com.developer.amukovozov.nerd.ui.screens.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.model.Feed
import com.developer.amukovozov.nerd.repository.FeedRepository
import com.developer.amukovozov.nerd.repository.LikeRepository
import com.developer.amukovozov.nerd.util.rx.schedulersIoToMain
import com.developer.amukovozov.nerd.util.ui.*
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val likeRepository: LikeRepository
) : BaseViewModel() {
    var viewState by mutableStateOf(FeedViewState())

    init {
        loadFeeds()
    }

    fun onLikeClicked(feedId: Int, isLiked: Boolean) {
        val feed = findFeedById(feedId) ?: return
        val (likeRequest, likesCount) = if (isLiked) {
            likeRepository.addLike(feedId) to feed.likesCount + 1
        } else {
            likeRepository.removeLike(feedId) to feed.likesCount - 1
        }

        val updatedFeed = feed.copy(isLiked = isLiked, likesCount = likesCount)
        updateFeed(updatedFeed)

        likeRequest
            .schedulersIoToMain()
            .subscribe({
                Timber.d("Success")
            }, {
                updateFeed(feed)
            })
            .disposeOnViewModelDestroy()
    }

    private fun loadFeeds() {
        feedRepository.loadFeed()
            .toObservable()
            .map<ScreenState<List<Feed>>> { Content(it) }
            .startWithItem(Loading())
            .doOnError(Timber::e)
            .onErrorReturn { Stub(it) }
            .schedulersIoToMain()
            .subscribe { screenState ->
                viewState = viewState.copy(screenState = screenState)
            }
            .disposeOnViewModelDestroy()
    }

    private fun updateFeed(feed: Feed) {
        val updatedList = getFeedList()?.map {
            if (it.id == feed.id) {
                feed
            } else {
                it
            }
        }
        viewState = viewState.copy(screenState = Content(updatedList))
    }

    private fun findFeedById(feedId: Int) = (viewState.screenState as? Content)?.content?.find { it.id == feedId }

    private fun getFeedList() = (viewState.screenState as? Content)?.content
}
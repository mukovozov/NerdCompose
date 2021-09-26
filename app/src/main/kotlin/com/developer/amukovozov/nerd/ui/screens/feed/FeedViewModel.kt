package com.developer.amukovozov.nerd.ui.screens.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.model.feed.Feed
import com.developer.amukovozov.nerd.network.pagination.Paginator
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
    private val likeRepository: LikeRepository,
    private val paginator: Paginator.Store<Feed>
) : BaseViewModel() {
    var viewState by mutableStateOf(FeedViewState())

    init {
        paginator.sideEffects
            .subscribe { effect ->
                when (effect) {
                    is Paginator.SideEffect.LoadPage -> loadNewPage(effect.currentPage)
                    is Paginator.SideEffect.ErrorEvent -> {
                        Timber.d(effect.error)
                    }
                }
            }
        paginator.proceed(Paginator.Action.Refresh)
    }

    fun onPulledToRefresh() {
        paginator.proceed(Paginator.Action.Refresh)
    }

    fun onPageEnded() {
        paginator.proceed(Paginator.Action.LoadMore)
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

    private fun loadNewPage(page: Int) {
        feedRepository.loadFeedPage(page)
            .toObservable()
            .map<ScreenState<List<Feed>>> {
                paginator.proceed(Paginator.Action.NewPage(page, it))
                val updatedList = getFeedList().toMutableList()
                updatedList.addAll(it)
                Content(updatedList)
            }
            .startWithItem(if (page == 0) Loading() else viewState.screenState)
            .doOnError(Timber::e)
            .onErrorReturn {
                paginator.proceed(Paginator.Action.PageError(it))
                Stub(it)
            }
            .schedulersIoToMain()
            .subscribe { screenState ->
                viewState = viewState.copy(screenState = screenState)
            }
            .disposeOnViewModelDestroy()
    }

    private fun updateFeed(feed: Feed) {
        val updatedList = getFeedList().map {
            if (it.id == feed.id) {
                feed
            } else {
                it
            }
        }
        viewState = viewState.copy(screenState = Content(updatedList))
    }

    private fun findFeedById(feedId: Int) = (viewState.screenState as? Content)?.content?.find { it.id == feedId }

    private fun getFeedList() = (viewState.screenState as? Content)?.content ?: emptyList()
}